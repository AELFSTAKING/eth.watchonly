package io.seg.kofo.ethwo.biz.controller;


import io.seg.kofo.api.request.QueryTransactionRequest;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.ethwo.biz.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 可用utxolist查询
 * 由gateway调用
 *
 * @author dongweizhao
 * @Date: 2018/9/24 下午9:46
 */
@Slf4j
@RestController
@RequestMapping("/queryTransaction")
public class QueryTransactionWeb extends BaseController<QueryTransactionRequest, String> {
    @Autowired
    private WalletService walletService;
    @Override
    protected RespData<String> call(QueryTransactionRequest request) {

        org.web3j.protocol.core.methods.response.EthTransaction transaction = null;
        try {
            transaction = walletService.queryTransaction(request.getHash());
            if(null == transaction){
                return RespData.success("");
            }
            return RespData.success(transaction.getTransaction().get().getHash());

        } catch (Exception e) {
            log.error("queryTransaction exception:{}",e.getMessage(),e);
            return RespData.error("999999",e.getMessage());
        }
    }
}
