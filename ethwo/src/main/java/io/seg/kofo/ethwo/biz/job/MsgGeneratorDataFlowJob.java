package io.seg.kofo.ethwo.biz.job;

import com.google.common.collect.Lists;
import io.seg.kofo.ethwo.biz.service.BlockCacheService;
import io.seg.kofo.ethwo.biz.service.BlockHeightService;
import io.seg.kofo.ethwo.biz.service.MsgQueueService;
import io.seg.kofo.ethwo.biz.service.SyncHeightService;
import io.seg.kofo.ethwo.common.config.FullNodeCache;
import io.seg.kofo.ethwo.common.config.WatchOnlyProperties;
import io.seg.kofo.ethwo.dao.po.BlockCachePo;
import io.seg.kofo.ethwo.dao.po.BlockHeightPo;
import io.seg.kofo.ethwo.dao.po.MsgQueuePo;
import io.seg.kofo.ethwo.dao.po.SyncHeightPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.Objects;

/**
 * 检查消息队列是否有空位
 * 有空位则流处理同步高度同时生成消息
 * @author gin
 */
@Slf4j
@Component
public class MsgGeneratorDataFlowJob {
    @Autowired
    MsgQueueService msgQueueService;
    @Autowired
    BlockCacheService blockCacheService;
    @Autowired
    BlockHeightService blockHeightService;
    @Autowired
    SyncHeightService syncHeightService;
    @Autowired
    WatchOnlyProperties watchOnlyProperties;
    @Autowired
    FullNodeCache fullNodeCache;

    @Scheduled(initialDelay = 4000, fixedDelay = 10000)
    public void excute() {
        int processCount = 0;

        log.info("generate block start:");

        while (true) {
            SyncHeightPo msgQueuePo = fetchData();

            if (Objects.isNull(msgQueuePo)) {
                log.info("generate block end,process {} block.", processCount);
                break;
            }
            processCount++;
            processData(msgQueuePo);
        }
    }

    public SyncHeightPo fetchData() {
        log.info("msgGenerator ready to fetchData");
        try {
            Integer msgQueueLimit = Integer.valueOf(watchOnlyProperties.getMsgQueueLimit());
            if (msgQueueLimit <= msgQueueService.countMsgQueue()){
                return null;
            }
            BlockHeightPo blockHeightPo = blockHeightService.selectOne(BlockHeightPo.builder().build());
            //表中只有一条数据
            SyncHeightPo syncHeightPo = syncHeightService.selectOne(SyncHeightPo.builder().build());
            if (syncHeightPo.getSyncHeight() >= blockHeightPo.getNodeLatestBlockHeight()){
                //同步高度大于全节点最高高度则不同步
                log.info("syncHeight:{} >= node height:{} ",syncHeightPo.getSyncHeight(),blockHeightPo.getNodeLatestBlockHeight());
                return null;
            }
            log.info("ready to syncHeight from :{} to:{}",syncHeightPo.getSyncHeight(),syncHeightPo.getSyncHeight()+1);
            //获取当前已同步到的高度进行后续处理
            return syncHeightPo;
        }catch (Exception e){
            log.error("msgGenerator fetchData exception:{}",e.getMessage(),e);
        }
        return null;
    }

    public void processData(SyncHeightPo syncHeightPo) {
        Web3j web3j = null;
        try {
            web3j = fullNodeCache.getWeb3j();
            EthBlock header = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(syncHeightPo.getSyncHeight()+1)),false).send();
            //要求syncHeight必须填写hash否则需要blockCache来修补
            //查询该区块前一个区块是否在syncHeight有记录
            SyncHeightPo preBlockHeightPo = syncHeightService.selectOne(SyncHeightPo.builder()
                    .blockHash(header.getBlock().getParentHash())
                    .build());
            BlockCachePo preBlockCachePo = null;
            // 如果没设置syncHeight表中blockHash 或者分叉
            if (null == preBlockHeightPo){
                //，在blockCache表中追溯分叉点
                preBlockCachePo = blockCacheService.selectOne(BlockCachePo.builder()
                        .blockHash(header.getBlock().getParentHash())
                        .build());
                if (null != preBlockCachePo){
                    //todo 区块缓存表中存在 但是syncheight.hash为空,或者syncheight.hash是分叉链
                }else {
                    //blockCache中没有，但是已经查询到创世区块了
                    if (header.getBlock().getNumber().equals(BigInteger.ONE)){
                        // 前一个区块如果未初始化创世区块 则在此添加
                        EthBlock zeroBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.ZERO),false).send();
                        blockCacheService.setGenesisBlockCache(zeroBlock);
                        return;
                    }
                }
            }

            boolean isForking = false;
            EthBlock preHeader = header;
            while (null == preBlockCachePo && null == preBlockHeightPo){
                //分叉
                isForking = true;
                //前溯一个区块--blockCache中不存在的区块
                log.info("ethGetBlockByhash:{}",preHeader.getBlock().getParentHash());
                preHeader = web3j.ethGetBlockByHash(preHeader.getBlock().getParentHash(),false).send();
                //todo blockcache为空应该报警处理否则死循环。
                preBlockCachePo = blockCacheService.selectOne(BlockCachePo.builder().blockHash(
                        preHeader.getBlock().getParentHash()
                ).build());
                //直到有blockCache记录-找到分叉点
            }
            boolean isSuccess = false;
            if (isForking){
                //todo 报警？ 此处eth的分叉区块为共有区块之以后的一个非共有区块
                EthBlock forkBlock = web3j.ethGetBlockByHash(preHeader.getBlock().getHash(),false).send();
                log.info("forking blockhash:{},height:{}",forkBlock.getBlock().getHash(),forkBlock.getBlock().getNumber());
                isSuccess = msgQueueService.generateForkingMsg(forkBlock.getBlock());
            }else {
                EthBlock syncBlock = web3j.ethGetBlockByHash(header.getBlock().getHash(),true).send();
                log.info("syncHeight blockhash:{},height:{}",syncBlock.getBlock().getHash(),syncBlock.getBlock().getNumber());
                isSuccess = msgQueueService.generateBlockMsg(syncBlock.getBlock());
            }
            log.info("syncHeight finish,result:{},isForking:{},processHeight:{}",isSuccess,isForking,header.getBlock().getNumber());
        } catch (Exception e) {
            log.error("MsgGeneratorDataFlowJob processData exception:{}",e.getMessage(),e);
        }


    }
}
