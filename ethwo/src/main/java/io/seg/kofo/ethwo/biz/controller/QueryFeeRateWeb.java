package io.seg.kofo.ethwo.biz.controller;

import io.seg.kofo.api.request.FeeRateRequest;
import io.seg.kofo.api.response.FeeRateResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.ethwo.biz.service.WalletService;
import io.seg.kofo.ethwo.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: ZhuYuanxiang
 * @create: 2019-03-11 15:37
 */
@Slf4j
@RestController
@RequestMapping("/feeRate")
public class QueryFeeRateWeb extends BaseController<FeeRateRequest, FeeRateResponse> {

    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<FeeRateResponse> call(FeeRateRequest request) {
        FeeRateResponse feeRateResponse = null;
        try {
            feeRateResponse = walletService.getFeeRate(request);
            return RespData.success(feeRateResponse);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}