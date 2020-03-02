package io.seg.kofo.ethwo.biz.controller;

import io.seg.kofo.api.request.AdminAccountRequest;
import io.seg.kofo.api.response.AdminAccountResponse;
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
@RequestMapping("/adminAccount")
public class QueryAdminAccountWeb extends BaseController<AdminAccountRequest, AdminAccountResponse> {
    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<AdminAccountResponse> call(AdminAccountRequest request) {
        AdminAccountResponse adminAccountResponse = null;
        try {
            adminAccountResponse = walletService.getAdminAccount(request);
            return RespData.success(adminAccountResponse);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
