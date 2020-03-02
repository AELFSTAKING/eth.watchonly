package io.seg.kofo.ethwo.biz.controller;


import io.seg.kofo.api.request.BalanceRequest;
import io.seg.kofo.api.request.QueryAddressNonceRequest;
import io.seg.kofo.api.response.BalanceResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import io.seg.kofo.ethwo.biz.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;


/**
 * @author gin
 */
@Slf4j
@RestController
@RequestMapping("/balance")
public class QueryBalanceWeb extends BaseController<BalanceRequest, BalanceResponse> {
    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<BalanceResponse> call(BalanceRequest request) {
        BalanceResponse balanceResponse = null;
        try {
            balanceResponse = walletService.getBalance(request);
            return RespData.success(balanceResponse);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
