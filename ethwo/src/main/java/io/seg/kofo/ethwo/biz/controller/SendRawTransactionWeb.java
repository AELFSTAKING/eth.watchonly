package io.seg.kofo.ethwo.biz.controller;


import io.seg.kofo.api.request.QueryAddressNonceRequest;
import io.seg.kofo.api.request.SendRawTransactionRequest;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import io.seg.kofo.common.exception.KofoCommonBizError;
import io.seg.kofo.ethwo.biz.service.WalletService;
import io.seg.kofo.ethwo.common.exception.EthBizCodeExcetion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

/**
 * 可用utxolist查询
 * 由gateway调用
 *
 * @author dongweizhao
 * @Date: 2018/9/24 下午9:46
 */
@Slf4j
@RestController
@RequestMapping("/sendRawTransaction")
public class SendRawTransactionWeb extends BaseController<SendRawTransactionRequest, String> {
    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<String> call(SendRawTransactionRequest request) {
        String txHash = null;
        try {

            txHash = walletService.sendRawTransaction(request.getRawTransaction());
        } catch (EthBizCodeExcetion e) {
            log.error("send raw  Transaction request:{} failed :{}", request, e.getMessage(), e);
            return RespData.error(e.getCode(), e.getDescription());
        } catch (Exception e) {
            log.error("send raw  Transaction request:{}  exception:{}", request, e.getMessage(), e);
            return RespData.error(KofoCommonBizError.BIZ_UNKNOWN_EXCEPTION.getCode(), e.getMessage());
        }
        return RespData.success(txHash);
    }
}
