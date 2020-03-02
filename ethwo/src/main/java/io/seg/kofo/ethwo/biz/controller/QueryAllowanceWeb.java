package io.seg.kofo.ethwo.biz.controller;


import io.seg.kofo.api.request.AllowanceRequest;
import io.seg.kofo.api.request.BalanceRequest;
import io.seg.kofo.api.response.AllowanceResponse;
import io.seg.kofo.api.response.BalanceResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import io.seg.kofo.ethwo.biz.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author gin
 */
@Slf4j
@RestController
@RequestMapping("/allowance")
public class QueryAllowanceWeb extends BaseController<AllowanceRequest, AllowanceResponse> {
    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<AllowanceResponse> call(AllowanceRequest request) {
        AllowanceResponse balanceResponse = null;
        try {
            balanceResponse = walletService.getAllowance(request);
            return RespData.success(balanceResponse);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
