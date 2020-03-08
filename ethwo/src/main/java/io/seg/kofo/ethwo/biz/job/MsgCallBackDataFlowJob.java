package io.seg.kofo.ethwo.biz.job;

import io.seg.kofo.ethwo.biz.service.BlockHeightService;
import io.seg.kofo.ethwo.biz.service.MsgQueueService;
import io.seg.kofo.ethwo.dao.po.MsgQueuePo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class MsgCallBackDataFlowJob  {
    @Autowired
    MsgQueueService msgQueueService;
    @Autowired
    BlockHeightService blockHeightService;

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void excute() {
        int processCount = 0;

        log.info("callback block start:");

        while (true) {
            MsgQueuePo msgQueuePo = fetchData();

            if (Objects.isNull(msgQueuePo)) {
                log.info("callback block end,process {} block.", processCount);
                break;
            }
            processCount++;
            processData(msgQueuePo);
        }
    }

    public MsgQueuePo fetchData() {
        try {
            MsgQueuePo msgQueuePo = msgQueueService.getMsgForCallBack().get(0);
            log.info("msg callback fetch data:{}", msgQueuePo.getHeight());
            return msgQueuePo;
        } catch (Exception e) {
            log.error("msg callback exception:{}", e.getMessage(), e);
        }
        return null;
    }

    public void processData( MsgQueuePo msgQueuePo) {
        try {
            boolean isCallbackSuccess = false;
            log.info("processData callbackBlockInfo requestHeight:{} ", msgQueuePo.getHeight());

            //todo callback biz msgQueuePo.getMsg()
            log.info("processData callbackBlockInfo requestHeight:{}", msgQueuePo.getHeight());
            if (isCallbackSuccess) {
                //更新msg回调状态 记录最新回调高度
                msgQueueService.updateCallBackInfo(msgQueuePo);
            }
        }catch (Exception e){
            log.error("msg call back error:{}",e.getMessage(),e);
        }



    }
}
