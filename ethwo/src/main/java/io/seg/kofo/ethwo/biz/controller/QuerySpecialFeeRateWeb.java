package io.seg.kofo.ethwo.biz.controller;

import io.seg.kofo.api.request.SpecialFeeRateRequest;
import io.seg.kofo.api.response.SpecialFeeRateResponse;
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
 * @create: 2019-03-11 15:42
 */
@Slf4j
@RestController
@RequestMapping("/specialFeeRate")
public class QuerySpecialFeeRateWeb extends BaseController<SpecialFeeRateRequest, SpecialFeeRateResponse> {

    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<SpecialFeeRateResponse> call(SpecialFeeRateRequest request) {
        SpecialFeeRateResponse specialFeeRateResponse = null;
        try {
            specialFeeRateResponse = walletService.getSpecialFeeRate(request);
            return RespData.success(specialFeeRateResponse);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}