package io.seg.kofo.ethwo.biz.controller;

import io.seg.kofo.api.request.FeeAccountRequest;
import io.seg.kofo.api.response.FeeAccountResponse;
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
 * @create: 2019-03-11 15:40
 */
@Slf4j
@RestController
@RequestMapping("/feeAccount")
public class QueryFeeAccountWeb extends BaseController<FeeAccountRequest, FeeAccountResponse> {
    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<FeeAccountResponse> call(FeeAccountRequest request) {
        FeeAccountResponse feeAccountResponse = null;
        try {
            feeAccountResponse = walletService.getFeeAccount(request);
            return RespData.success(feeAccountResponse);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
