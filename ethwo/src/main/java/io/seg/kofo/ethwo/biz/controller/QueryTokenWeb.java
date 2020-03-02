package io.seg.kofo.ethwo.biz.controller;


import io.seg.kofo.api.request.TokenRequest;
import io.seg.kofo.api.response.TokenResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import io.seg.kofo.ethwo.biz.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author zhuyuanxiang
 */
@Slf4j
@RestController
@RequestMapping("/token")
public class QueryTokenWeb extends BaseController<TokenRequest, TokenResponse> {
    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<TokenResponse> call(TokenRequest request) {
        TokenResponse tokenResponse = null;
        try {
            tokenResponse = walletService.getToken(request);
            return RespData.success(tokenResponse);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
