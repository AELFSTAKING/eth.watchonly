package io.seg.kofo.ethwo.common.config;

import com.google.common.cache.*;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.seg.kofo.ethwo.biz.service.BlockHeightService;
import io.seg.kofo.ethwo.common.util.GethRpcClient;
import io.seg.kofo.ethwo.dao.po.BlockHeightPo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.JsonRpc2_0Admin;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1.在原有节点不lag的情况下 继续使用原有节点
 * 2.
 *
 * @author gin
 */
@Slf4j
@Component
public class FullNodeCache {

    private final WatchOnlyProperties watchOnlyProperties;

    private final BlockHeightService blockHeightService;

    private static Lock lock = new ReentrantLock();

    /**
     * 当前节点ip:port
     */
    private String currentNode;

    private Web3j web3j;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(25, 10, TimeUnit.SECONDS))
            .build();

    @Autowired
    public FullNodeCache(WatchOnlyProperties watchOnlyProperties, BlockHeightService blockHeightService) {
        this.watchOnlyProperties = watchOnlyProperties;
        this.blockHeightService = blockHeightService;
    }


    /**
     * 遍历配置的节点高度，判断是否需要替换currentNode
     */
    @Scheduled(fixedRate = 30000, initialDelay = 25000)
    public void refreshCurrentNode() {
        log.info("refreshCurrentNode ...");
        String bestNode;
        lock.lock();
        try {
            //检测获取最佳节点
            bestNode = loadBest(currentNode);
            if (bestNode.equals(currentNode)) {
                //最佳节点与原有节点相等，不需要刷新
                return;
            }
            // 刷新当前web3j，需要先shutdown 否则可能内存泄漏
            this.currentNode = bestNode;
            try {
                if (null != this.web3j) {
                    this.web3j.shutdown();
                }
            } catch (Exception e) {
                log.error("web3j shutdown exception :", e.getMessage(), e);
            }
            HttpService httpService = new HttpService("http://" + bestNode, okHttpClient);
            this.web3j = new JsonRpc2_0Web3j(httpService);
        } catch (Exception e) {
            log.error("refreshCurrentNode exception :", e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 兼具初次加载/反复刷新的功能
     *
     * @param oldValue optional 初次加载可以传入null
     * @return
     * @throws Exception
     */
    public String loadBest(String oldValue) throws Exception {

        //表中只有一条height记录
        BlockHeightPo blockHeightPo = blockHeightService.selectOne(BlockHeightPo.builder().build());
        //先判断原有的cache需不需要刷新
        String fullNodes = watchOnlyProperties.getRpcNodes();


        String[] fullNodeGroup = fullNodes.split(",");
        Map<String, Long> availableNodeHeight = new HashMap<>();
        long heighest = 0;
        String heighestNode = null;
        //遍历请求各个节点高度 同时选择最高节点
        for (String nodeUrlString : fullNodeGroup) {
            try {
                long blockCount = getValideNodeHeight(blockHeightPo, nodeUrlString);
                availableNodeHeight.put(nodeUrlString, blockCount);
                if (blockCount > heighest) {
                    heighest = blockCount;
                    heighestNode = nodeUrlString;
                }
            } catch (Exception e) {
                log.error(">>>node unavailable<<< node:[{}]", nodeUrlString);
            }
        }
        log.info("available node info:{}", availableNodeHeight);
        if (heighestNode == null) {
            throw new RuntimeException("heighestNode is null");
        }
        if (oldValue != null) {
            //如果原节点不为空 则检查原节点高度--刷新原节点
            String originNode = oldValue;
            GethRpcClient originClient = new GethRpcClient(originNode);
            long originNodeHeight = originClient.getBlockCount();
            if (heighest - originNodeHeight < watchOnlyProperties.getLagThreshold() && heighest - originNodeHeight >= 0) {
                //如果最高节点高度-原节点高度 大于等于0 小于阈值 则使用原节点
                log.info("origin node:{}, height:{} heighest:{},lagthreshold:{},no neeed refresh", originNode, originNodeHeight, heighest, watchOnlyProperties.getLagThreshold());
                return originNode;
            } else {
                // heighest  < originNodeHeight
                // heighest - originNodeHeight > threshold
            }
        }

        return heighestNode;
    }

    /**
     * 检查外部节点高度
     * 报警处理
     */
    private void checkOutterLatestHeight(Long outterLatestBlockHeight, Long currentNodeHeight, String currentNodeString) {
        if (outterLatestBlockHeight - currentNodeHeight <= watchOnlyProperties.getLagThreshold()) {
            //常出现全节点更新速度更快的情况，
            if (outterLatestBlockHeight - currentNodeHeight >= -1) {
                //如果与外部节点高度相差不大
                log.info("latestHeight:{} current Node :{} height:{} lagged less than threshold:{} , no need refresh fullNodeCache", outterLatestBlockHeight, currentNodeString, currentNodeHeight, watchOnlyProperties.getLagThreshold());
            } else {
                //todo 外部节点高度低于我们自己节点 报警
                log.error(">>>lagged<<< latestHeight :{} far less than current node :{} height:{},outer-node or current-node maybe wrong", outterLatestBlockHeight, currentNodeString, currentNodeHeight);
            }
        } else {
            //todo 外部节点高度远高于于我们自己节点 报警
            log.warn(">>>lagged<<< latestHeight:{} current Node :{} height:{} lagged more than threshold:{},should refresh fullNodeCache", outterLatestBlockHeight, currentNodeString, currentNodeHeight, watchOnlyProperties.getLagThreshold());
        }
    }

    private long getValideNodeHeight(BlockHeightPo blockHeightPo, String nodeUrlString) throws Exception {
        GethRpcClient gethRpcClient = new GethRpcClient(nodeUrlString);
        long blockCount = gethRpcClient.getBlockCount();
        //先检查与外部最新高度的差距
        checkOutterLatestHeight(blockHeightPo.getLatestBlockHeight(), blockCount, nodeUrlString);
        return blockCount;
    }

    public GethRpcClient getEthClient() {
        if (null == currentNode) {
            lock.lock();
            if (null == currentNode) {
                refreshCurrentNode();
            }
            lock.unlock();
        }
        return new GethRpcClient(currentNode);
    }

    public Web3j getWeb3j() {
        if (null == web3j) {
            lock.lock();
            if (null == web3j) {
                refreshCurrentNode();
            }
            lock.unlock();
        }
        return web3j;
    }


}
