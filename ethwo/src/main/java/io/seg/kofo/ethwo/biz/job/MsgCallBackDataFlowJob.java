package io.seg.kofo.ethwo.biz.job;

import io.seg.kofo.ethereum.gateway.api.integration.EthAnalyzerClient;
import io.seg.kofo.ethereum.gateway.api.vo.request.MessageReq;
import io.seg.kofo.ethwo.biz.service.BlockHeightService;
import io.seg.kofo.ethwo.biz.service.MsgQueueService;
import io.seg.kofo.ethwo.common.util.TraceIdUtil;
import io.seg.kofo.ethwo.dao.po.MsgQueuePo;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import io.seg.kofo.common.controller.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MsgCallBackDataFlowJob implements DataflowJob {
    @Autowired
    MsgQueueService msgQueueService;
    @Autowired
    BlockHeightService blockHeightService;
    @Autowired
    EthAnalyzerClient ethAnaLyzerClient;

    @Override
    public List fetchData(ShardingContext shardingContext) {
        try {
            List list = msgQueueService.getMsgForCallBack();
            log.info("msg callback fetch data:{}", list.size());
            return list;
        } catch (Exception e) {
            log.error("msg callback exception:{}", e.getMessage(), e);
        }
        return new ArrayList();
    }

    @Override
    public void processData(ShardingContext shardingContext, List list) {


        try {
            TraceIdUtil.startTrace();
            MsgQueuePo msgQueuePo = (MsgQueuePo) list.get(0);
            MessageReq req = new MessageReq();
//        if (MsgTypeEnum.BLOCK_MSG.getCode().intValue() == msgQueuePo.getMsgType()){
//        }
            req.setData(msgQueuePo.getMsg());
            req.setType(String.valueOf(msgQueuePo.getMsgType()));
            log.info("processData callbackBlockInfo requestHeight:{} req size:{}", msgQueuePo.getHeight(), req.toString().length());

            //feign调用
            RespData<Integer> respData = ethAnaLyzerClient.message(req);
            log.info("processData callbackBlockInfo requestHeight:{} response:{}", msgQueuePo.getHeight(), respData);
            if (respData.isSuccess()) {
                //更新msg回调状态 记录最新回调高度
                msgQueueService.updateCallBackInfo(msgQueuePo);
            }
        }catch (Exception e){
            log.error("msg call back error:{}",e.getMessage(),e);
        }finally {
            TraceIdUtil.endTrace();
        }



    }
}
