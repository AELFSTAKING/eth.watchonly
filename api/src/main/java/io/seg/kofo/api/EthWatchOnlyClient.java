package io.seg.kofo.api;

import io.seg.kofo.api.request.*;
import io.seg.kofo.api.response.*;
import io.seg.kofo.common.controller.RespData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "eth-wo", configuration = FeignClientLoggingConfiguration.class)
public interface EthWatchOnlyClient {

    /**
     * 获取最新的高度，这个高度是通过各种方式获取到最新的区块高度，不仅仅是查询自己的全节点
     *
     * @return
     */
    @GetMapping("/height")
    RespData<Long> lastestHeight();

    @RequestMapping("/queryAddressNonce")
    RespData<Long> queryAddressNonce(@RequestBody QueryAddressNonceRequest queryAddressNonceRequest);
    @RequestMapping("/queryTransaction")
    RespData<String> queryTransaction(@RequestBody QueryTransactionRequest queryTransactionRequest);

    @RequestMapping("/queryTransactionDetails")
    RespData<QueryTransactionResponse> queryTransactionDetails(@RequestBody QueryTransactionRequest queryTransactionRequest);

    @RequestMapping("/queryTransactionReceipts")
    RespData<QueryTransactionReceiptResponse> queryTransactionReceipts(@RequestBody QueryTransactionRequest queryTransactionRequest);

    @RequestMapping("/sendRawTransaction")
    RespData<String> sendRawTransaction(@RequestBody SendRawTransactionRequest rawTransaction);

    @RequestMapping("/sendTransaction")
    RespData<String> sendTransaction(@RequestBody @Valid org.web3j.protocol.core.methods.request.Transaction transaction);

    @RequestMapping("/gasPrice")
    RespData<String> getGasPrice();

    @RequestMapping("/balance")
    RespData<BalanceResponse> balance(@RequestBody @Valid BalanceRequest balanceRequest);

    @RequestMapping("/allowance")
    RespData<AllowanceResponse> allowance(@RequestBody @Valid AllowanceRequest allowanceRequest);

    @RequestMapping("/token")
    RespData<TokenResponse> queryToken(@RequestBody @Valid TokenRequest tokenRequest);

    @RequestMapping("/feeRate")
    RespData<FeeRateResponse> getFeeRate(@RequestBody @Valid FeeRateRequest feeRateRequest);

    @RequestMapping("/feeAccount")
    RespData<FeeAccountResponse> getFeeAccount(@RequestBody @Valid FeeAccountRequest feeAccountRequest);

    @RequestMapping("/adminAccount")
    RespData<AdminAccountResponse> getAdminAccount(@RequestBody @Valid AdminAccountRequest adminAccountRequest);

    @RequestMapping("/specialFeeRate")
    RespData<SpecialFeeRateResponse> getSpecialFeeRate(@RequestBody @Valid SpecialFeeRateRequest specialFeeRateRequest);
}
