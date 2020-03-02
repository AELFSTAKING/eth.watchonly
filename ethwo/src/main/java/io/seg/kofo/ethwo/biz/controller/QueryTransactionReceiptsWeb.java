package io.seg.kofo.ethwo.biz.controller;


import io.seg.kofo.api.request.QueryTransactionRequest;
import io.seg.kofo.api.response.QueryTransactionReceiptResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.ethwo.biz.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * 可用utxolist查询
 * 由gateway调用
 *
 * @author dongweizhao
 * @Date: 2018/9/24 下午9:46
 */
@Slf4j
@RestController
@RequestMapping("/queryTransactionReceipts")
public class QueryTransactionReceiptsWeb extends BaseController<QueryTransactionRequest, QueryTransactionReceiptResponse> {
    @Autowired
    private WalletService walletService;
    @Override
    protected RespData<QueryTransactionReceiptResponse> call(QueryTransactionRequest request) {

        TransactionReceipt transaction = null;
        try {
            transaction = walletService.queryTransactionReceipts(request.getHash());
            if(null == transaction){
                return RespData.success(null);
            }
            QueryTransactionReceiptResponse response = new QueryTransactionReceiptResponse();

            response.setGasUsed(transaction.getGasUsed().toString(10));
            response.setStatus(transaction.getStatus());
            response.setTransactionHash(transaction.getTransactionHash());
            return RespData.success(response);

        } catch (Exception e) {
            log.error("queryTransaction exception:{}",e.getMessage(),e);
            return RespData.error("999999",e.getMessage());
        }
    }
}
