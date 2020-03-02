package io.seg.kofo.ethwo.biz.controller;


import io.seg.kofo.api.request.QueryTransactionRequest;
import io.seg.kofo.api.response.QueryTransactionResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
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
@RequestMapping("/queryTransactionDetails")
public class QueryTransactionDetailWeb extends BaseController<QueryTransactionRequest, QueryTransactionResponse> {
    @Autowired
    private WalletService walletService;

    @Override
    protected RespData<QueryTransactionResponse> call(QueryTransactionRequest request) {

        org.web3j.protocol.core.methods.response.EthTransaction transaction;
        try {
            transaction = walletService.queryTransaction(request.getHash());
            if (null == transaction) {
                return RespData.success(null);
            }

            QueryTransactionResponse queryTransactionResponse = new QueryTransactionResponse();

            queryTransactionResponse.setHash(transaction.getTransaction().get().getHash());
            queryTransactionResponse.setNonce(transaction.getTransaction().get().getNonce().toString());
            queryTransactionResponse.setBlockHash(transaction.getTransaction().get().getBlockHash());
            if (transaction.getTransaction().get().getBlockNumberRaw() != null) {
                queryTransactionResponse.setBlockNumber(transaction.getTransaction().get().getBlockNumber().toString());
            }
            queryTransactionResponse.setTransactionIndex(transaction.getTransaction().get().getTransactionIndex().toString());
            queryTransactionResponse.setFrom(transaction.getTransaction().get().getFrom());
            queryTransactionResponse.setTo(transaction.getTransaction().get().getTo());
            queryTransactionResponse.setValue(transaction.getTransaction().get().getValue().toString());
            queryTransactionResponse.setGasPrice(transaction.getTransaction().get().getGasPrice().toString());
            queryTransactionResponse.setGas(transaction.getTransaction().get().getGas().toString());
            queryTransactionResponse.setInput(transaction.getTransaction().get().getInput());
            queryTransactionResponse.setCreates(transaction.getTransaction().get().getCreates());
            queryTransactionResponse.setPublicKey(transaction.getTransaction().get().getPublicKey());
            queryTransactionResponse.setRaw(transaction.getTransaction().get().getRaw());
            queryTransactionResponse.setR(transaction.getTransaction().get().getR());
            queryTransactionResponse.setS(transaction.getTransaction().get().getS());
            queryTransactionResponse.setV((int) transaction.getTransaction().get().getV());
            return RespData.success(queryTransactionResponse);
        } catch (Exception e) {
            log.error("queryTransaction exception:{}", e.getMessage(), e);
            return RespData.error("999999", e.getMessage());
        }
    }
}
