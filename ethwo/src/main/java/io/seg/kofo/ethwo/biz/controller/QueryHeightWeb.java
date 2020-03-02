package io.seg.kofo.ethwo.biz.controller;



import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.KofoCommonBizError;
import io.seg.kofo.ethwo.biz.service.WalletService;
import io.seg.kofo.ethwo.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



/**
 * 可用utxolist查询
 * 由gateway调用
 *
 * @author dongweizhao
 * @Date: 2018/9/24 下午9:46
 */
@Slf4j
@RestController
@RequestMapping("/height")
public class QueryHeightWeb {
    @Autowired
    private WalletService walletService;

    @RequestMapping({""})
    @ResponseBody
    protected RespData<Long> call() {
        Long height;
        try {
            height = walletService.getLatestBlockNum();
            return RespData.success(height);
        } catch (BizException e) {
            log.error("query local height failed : {}", e);
            return RespData.error(String.valueOf(e.getCode()), e.getMessage());
        } catch (Exception e) {
            log.error("query local height failed : {}", e);
            return RespData.error(KofoCommonBizError.BIZ_UNKNOWN_EXCEPTION.getCode(), e.getMessage());
        }
    }
}
