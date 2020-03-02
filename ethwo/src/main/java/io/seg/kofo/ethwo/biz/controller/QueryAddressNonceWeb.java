package io.seg.kofo.ethwo.biz.controller;



import io.seg.kofo.api.request.QueryAddressNonceRequest;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import io.seg.kofo.ethwo.biz.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@Slf4j
@RestController
@RequestMapping("/queryAddressNonce")
public class QueryAddressNonceWeb extends BaseController<QueryAddressNonceRequest, Long> {
    @Autowired
    private WalletService walletService;
    @Override
    protected RespData<Long> call(QueryAddressNonceRequest request) {
        BigInteger result = null;
        try {
            result = walletService.queryAddressNounce(request.getAddress());
            return RespData.success(result.longValue());
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
