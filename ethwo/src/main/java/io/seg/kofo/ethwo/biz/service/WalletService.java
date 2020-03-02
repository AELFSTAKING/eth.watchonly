package io.seg.kofo.ethwo.biz.service;

import io.seg.kofo.api.request.*;
import io.seg.kofo.api.response.*;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author WalkerLiu
 * @date 2018/9/20
 */
public interface WalletService {


    /**
     *
     * @param transaction :transaction wait to send
     * @return transaction hash
     * @throws IOException
     */
    String sendTransaction(Transaction transaction) throws Exception;

    /**
     * 获取
     * @return
     */
    Long getLatestBlockNum() throws Exception;


    Long globalHeight();

    BalanceResponse getBalance(BalanceRequest balanceRequest) throws Exception;

     AllowanceResponse getAllowance(AllowanceRequest allowanceRequest) throws Exception;

    TokenResponse getToken(TokenRequest tokenRequest) throws Exception;

    /**
     * 获取手续费率
     * @return
     * @throws Exception
     */
    FeeRateResponse getFeeRate(FeeRateRequest feeRateRequest) throws Exception;

    /**
     * 获取手续费接收地址
     * @return
     * @throws Exception
     */
    FeeAccountResponse getFeeAccount(FeeAccountRequest feeAccountRequest) throws Exception;

    /**
     * 获取admin地址
     * @return
     * @throws Exception
     */
    AdminAccountResponse getAdminAccount(AdminAccountRequest feeAccountRequest) throws Exception;

    /**
     * 获取分级用户手续费
     * @param specialFeeRateRequest
     * @return
     * @throws Exception
     */
    SpecialFeeRateResponse getSpecialFeeRate(SpecialFeeRateRequest specialFeeRateRequest) throws Exception;

    String gasPrice() throws IOException;

    String sendRawTransaction(String rawTransaction) throws Exception;

    org.web3j.protocol.core.methods.response.EthTransaction queryTransaction(String hash) throws Exception;

    TransactionReceipt queryTransactionReceipts(String hash) throws Exception;

    BigInteger queryAddressNounce(String address) throws Exception;
}
