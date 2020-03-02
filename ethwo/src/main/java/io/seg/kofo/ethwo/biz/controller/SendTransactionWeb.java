package io.seg.kofo.ethwo.biz.controller;


import com.alibaba.fastjson.JSON;
import io.seg.kofo.api.request.SendRawTransactionRequest;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import io.seg.kofo.common.exception.KofoCommonBizError;
import io.seg.kofo.ethwo.biz.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.request.Transaction;

/**
 * 可用utxolist查询
 * 由gateway调用
 *
 * @author dongweizhao
 * @Date: 2018/9/24 下午9:46
 */
@Slf4j
@RestController
@RequestMapping("/sendTransaction")
public class SendTransactionWeb extends BaseController<Transaction, String> {
    @Autowired
    private WalletService walletService;
    @Override
    protected RespData<String> call(Transaction request) {

        String txHash = null;
        try {
            txHash = walletService.sendTransaction(request);
            return RespData.success(txHash);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
