package io.seg.kofo.ethwo.biz.job;

import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.ethwo.biz.service.BlockHeightService;
import io.seg.kofo.ethwo.biz.service.WalletService;
import io.seg.kofo.ethwo.common.config.FullNodeCache;
import io.seg.kofo.ethwo.common.config.WatchOnlyProperties;
import io.seg.kofo.ethwo.common.util.Numeric;
import io.seg.kofo.ethwo.dao.po.BlockHeightPo;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.util.Date;


/**
 * 更新本地全节点高度
 * 回调高度
 *
 * @author gin
 */
@Slf4j
@Component
public class BlockHeightUpdateJob  {
    @Autowired
    BlockHeightService blockHeightService;
    @Autowired
    WatchOnlyProperties watchOnlyProperties;

    @Autowired
    WalletService walletService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    FullNodeCache fullNodeCache;


    public void execute() {
        try {
            //默认只有一条记录
            BlockHeightPo blockHeightPo = blockHeightService.selectOne(BlockHeightPo.builder().build());
            updateNodeHeight(blockHeightPo);
            updateLatestHeight(blockHeightPo);
            //todo callback biz  block-height;
        } catch (Exception e) {
            log.error("BlockHeightUpdateJob exception:{}", e.getMessage(), e);
        }

    }

    private boolean updateNodeHeight(BlockHeightPo blockHeightPo) {
        try {
            EthBlockNumber blockNumber = fullNodeCache.getWeb3j().ethBlockNumber().send();
            //更新本地节点高度
            blockHeightPo.setNodeLatestBlockHeight(blockNumber.getBlockNumber().longValueExact());
            blockHeightPo.setUpdateTime(new Date());
            blockHeightService.update(blockHeightPo);
            log.info("update NodeHeight to:{}", blockHeightPo.getNodeLatestBlockHeight());
        } catch (Exception e) {
            log.warn("updateNodeHeight exception:{}", e.getMessage(), e);
        }
        return true;

    }

    private boolean updateLatestHeight(BlockHeightPo blockHeightPo) {
        try {
            JSONObject response = restTemplate.getForEntity(watchOnlyProperties.getLatestBlockUrl(), JSONObject.class).getBody();
            log.info("latestBlock url:{} response:>>>{}", watchOnlyProperties.getLatestBlockUrl(), response.toJSONString());
            if (null != response) {
                //更新最高区块高度
                String result = (String) response.get("result");
                long latestHeight = Long.parseLong(Numeric.toBigInt(result).toString(), 10);
                blockHeightPo.setLatestBlockHeight(latestHeight);
                blockHeightPo.setUpdateTime(new Date());
                blockHeightService.update(blockHeightPo);
                log.info("update latestHeight to:{}", blockHeightPo);
            }
        } catch (Exception e) {
            log.warn("updateLatestHeight exception:{}", e.getMessage(), e);
        }
        return true;
    }
}


