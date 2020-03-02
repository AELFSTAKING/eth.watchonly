package io.seg.kofo.ethwo.biz.controller;


import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.KofoCommonBizError;
import io.seg.kofo.ethwo.biz.service.WalletService;
import io.seg.kofo.ethwo.common.config.WatchOnlyProperties;
import io.seg.kofo.ethwo.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/gasPrice")
public class QueryGasPriceWeb {
    @Autowired
    private WalletService walletService;

    @RequestMapping({""})
    @ResponseBody
    protected RespData<String> call() {
        log.info("gasprice request");
        String gasPrice;
        try {
            gasPrice = walletService.gasPrice();
            log.info("gasprice response:{}",gasPrice);
            return RespData.success(gasPrice);
        } catch (BizException e) {
            log.error("query local height failed : {}", e);
            return RespData.error(String.valueOf(e.getCode()),e.getMessage());
        } catch (Exception e) {
            log.error("query local height failed : {}", e);
            return RespData.error(KofoCommonBizError.BIZ_UNKNOWN_EXCEPTION.getCode(),e.getMessage());
        }
    }
}
