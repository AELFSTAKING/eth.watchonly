package io.seg.kofo.ethwo.biz.service.impl;

import io.seg.kofo.api.request.*;
import io.seg.kofo.api.response.*;
import io.seg.kofo.ethwo.biz.service.BlockHeightService;
import io.seg.kofo.ethwo.biz.service.WalletService;
import io.seg.kofo.ethwo.common.config.FullNodeCache;
import io.seg.kofo.ethwo.common.config.WatchOnlyProperties;
import io.seg.kofo.ethwo.common.util.ERC20FunctionUtils;
import io.seg.kofo.ethwo.common.util.HandleErrorUtils;
import io.seg.kofo.ethwo.common.util.HashLockContractUtil;
import io.seg.kofo.ethwo.dao.po.BlockHeightPo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @date 2018/9/20
 */
@Service
@Slf4j
public class WalletServiceImpl implements WalletService {
    @Autowired
    private ERC20FunctionUtils erc20FunctionUtils;
    @Autowired
    private HandleErrorUtils handleErrorUtils;
    @Autowired
    private FullNodeCache fullNodeCache;
    @Autowired
    private BlockHeightService blockHeightService;
    @Autowired
    private HashLockContractUtil hashLockContractUtil;

    private static final String ETH_TRUE_RETURN = "0x0000000000000000000000000000000000000000000000000000000000000001";

    private static final String ETH_FALUSE_RETURN = "0x0000000000000000000000000000000000000000000000000000000000000000";

    @Override
    public String sendTransaction(Transaction transaction) throws Exception {
        EthSendTransaction response = fullNodeCache.getWeb3j().ethSendTransaction(transaction).send();
        if (response.getResult() == null && response.getError() != null) {
            handleErrorUtils.handResponseError(response);
        } else if (response.getResult() == null && response.getError() == null) {
            throw new Exception("Unkown error ");
        }
        return response.getTransactionHash();
    }


    @Override
    public Long getLatestBlockNum() throws Exception {
        BlockHeightPo blockHeight = blockHeightService.selectOne(BlockHeightPo.builder().build());
        EthBlockNumber response = fullNodeCache.getWeb3j().ethBlockNumber().send();
        if (response.getResult() == null && response.getError() != null) {
            handleErrorUtils.handResponseError(response);
        } else if (response.getResult() == null && response.getError() == null) {
            throw new Exception("Unkown error ");
        }
        return Math.max(response.getBlockNumber().longValue(), blockHeight.getLatestBlockHeight());
    }

    @Override
    public Long globalHeight() {
        //TODO : get global height
        return null;
    }

    @Override
    public BalanceResponse getBalance(BalanceRequest balanceRequest) throws Exception {
        BalanceResponse response = new BalanceResponse();
        response.setAddress(balanceRequest.getAddress());
        response.setTokenAddress(balanceRequest.getTokenAddress());
        if (StringUtils.isBlank(balanceRequest.getTokenAddress())) {
            log.info("get eth balance:{}", balanceRequest);
            EthGetBalance ethGetBalance = fullNodeCache.getWeb3j().ethGetBalance(balanceRequest.getAddress(), DefaultBlockParameterName.LATEST).send();
            if (ethGetBalance.getError() != null) {
                handleErrorUtils.handResponseError(ethGetBalance);
            }
            if (ethGetBalance.getBalance() == null) {
                throw new Exception("exception occur get balance is null pointer");
            }
            response.setBalance(Convert.fromWei(new BigDecimal(ethGetBalance.getBalance()), Convert.Unit.ETHER).toPlainString());
        } else {
            log.info("get erc20 balance:{}", balanceRequest);
            String balanceStr = splitOutValue(erc20FunctionUtils.balanceOf(balanceRequest.getTokenAddress(), balanceRequest.getAddress()));
            if (StringUtils.isBlank(balanceStr)) {
                log.info("balanceStr is null, balance of {} for token {} may be 0", balanceRequest, balanceRequest.getTokenAddress());
                return response;
            }
            BigInteger balance = new BigInteger(balanceStr, 16);
            balanceStr = balance.toString();
            response.setBalance(balanceStr);

            String decimal = erc20FunctionUtils.getDecimal(balanceRequest.getTokenAddress());
            if (StringUtils.isBlank(decimal)) {
                log.error("decimal is null");
                return response;
            }
            String valueStr = splitOutValue(decimal);
            BigDecimal balanceResult = new BigDecimal(balance);
            balanceResult = balanceResult.divide(BigDecimal.TEN.pow(Integer.parseInt(valueStr, 16)));
            response.setBalance(balanceResult.toPlainString());
        }
        return response;
    }

    @Override
    public AllowanceResponse getAllowance(AllowanceRequest allowanceRequest) throws Exception {
        AllowanceResponse response = new AllowanceResponse();
        response.setOwnerAddress(allowanceRequest.getOwnerAddress());
        response.setSpenderAddress(allowanceRequest.getSpenderAddress());
        response.setTokenAddress(allowanceRequest.getTokenAddress());
        log.info("get erc20 allowance:{}", allowanceRequest);
        String balanceStr = splitOutValue(erc20FunctionUtils.allowance(allowanceRequest.getTokenAddress(), allowanceRequest.getOwnerAddress(),allowanceRequest.getSpenderAddress()));
        if (StringUtils.isBlank(balanceStr)) {
            log.info("allowanceStr is null, allowance of {} for token {} may be 0", allowanceRequest, allowanceRequest.getTokenAddress());
            return response;
        }
        BigInteger balance = new BigInteger(balanceStr, 16);
        balanceStr = balance.toString();
        response.setBalance(balanceStr);

        String decimal = erc20FunctionUtils.getDecimal(allowanceRequest.getTokenAddress());
        if (StringUtils.isBlank(decimal)) {
            log.error("decimal is null");
            return response;
        }
        String valueStr = splitOutValue(decimal);
        BigDecimal balanceResult = new BigDecimal(balance);
        balanceResult = balanceResult.divide(BigDecimal.TEN.pow(Integer.parseInt(valueStr, 16)));
        response.setBalance(balanceResult.toPlainString());
        return response;
    }

    @Override
    public TokenResponse getToken(TokenRequest tokenRequest) throws Exception {
        String tokenAddress = tokenRequest.getTokenAddress();
        TokenResponse response = new TokenResponse();
        response.setTokenAddress(tokenAddress);

        String totalSupply = erc20FunctionUtils.totalSupply(tokenAddress);
        if (StringUtils.isBlank(totalSupply)) {
            log.error("totalSupply is null");
            return response;
        }
        response.setTotalSupply(totalSupply);

        String decimals = erc20FunctionUtils.getDecimal(tokenAddress);
        if (StringUtils.isBlank(decimals)) {
            log.error("decimals is null");
            return response;
        }
        response.setDecimals(decimals);

        String name = erc20FunctionUtils.getName(tokenAddress);
        if (StringUtils.isBlank(name)) {
            log.error("name is null");
            return response;
        }
        response.setName(name);

        return response;
    }

    @Override
    public FeeRateResponse getFeeRate(FeeRateRequest feeRateRequest) throws Exception {
        FeeRateResponse response = new FeeRateResponse();
        String contractInvokeResult = hashLockContractUtil.queryFeeRate(feeRateRequest.getContractAddress());
        String decode = splitOutValue(contractInvokeResult);
        String feeRateStr = decode.equals("") ? "0" : decode;
        if (StringUtils.isBlank(feeRateStr)) {
            log.error("feeRateStr is null");
            return response;
        }
        BigInteger feeRateBigInt = new BigInteger(feeRateStr, 16);
        response.setFeeRate(feeRateBigInt.toString());
        return response;
    }

    @Override
    public FeeAccountResponse getFeeAccount(FeeAccountRequest feeAccountRequest) throws Exception {
        FeeAccountResponse response = new FeeAccountResponse();
        String feeAccountStr = "0x" + splitOutValue(hashLockContractUtil.queryFeeAccount(feeAccountRequest.getContractAddress()));
        if (StringUtils.isBlank(feeAccountStr)) {
            log.error("feeAccountStr is null");
            return response;
        }
        response.setFeeAccount(feeAccountStr);
        return response;
    }

    @Override
    public AdminAccountResponse getAdminAccount(AdminAccountRequest adminAccountRequest) throws Exception {
        AdminAccountResponse response = new AdminAccountResponse();
        String feeAccountStr = "0x" + splitOutValue(hashLockContractUtil.queryAdminAccount(adminAccountRequest.getContractAddress()));
        if (StringUtils.isBlank(feeAccountStr)) {
            log.error("adminAccountStr is null");
            return response;
        }
        response.setAdminAccount(feeAccountStr);
        return response;
    }

    @Override
    public SpecialFeeRateResponse getSpecialFeeRate(SpecialFeeRateRequest specialFeeRateRequest) throws Exception {
        SpecialFeeRateResponse response = new SpecialFeeRateResponse();
        String isSpecialStr = hashLockContractUtil.queryIsSpecialAddress(specialFeeRateRequest.getContractAddress(), specialFeeRateRequest.getSpecialAddress());
        if (!isSpecial(isSpecialStr)) {
            response.setSpecial(false);
            response.setSpecialFeeRate("0");
        } else {
            String contractInvokeResult = hashLockContractUtil.querySpecialFeeRate(specialFeeRateRequest.getContractAddress(), specialFeeRateRequest.getSpecialAddress());
            String decode = splitOutValue(contractInvokeResult);
            String specialFeeRateStr = decode.equals("") ? "0" : decode;
            if (StringUtils.isBlank(specialFeeRateStr)) {
                log.error("specialFeeRateStr is null");
                return response;
            }
            BigInteger feeRateBigInt = new BigInteger(specialFeeRateStr, 16);
            response.setSpecial(true);
            response.setSpecialFeeRate(feeRateBigInt.toString());
        }
        return response;
    }

    @Override
    public String gasPrice() throws IOException {
        EthGasPrice gasPrice = fullNodeCache.getWeb3j().ethGasPrice().send();
        return gasPrice.getGasPrice().toString();
    }

    @Override
    public String sendRawTransaction(String rawTransaction) throws Exception {
        EthSendTransaction response = null;

        response = fullNodeCache.getWeb3j().ethSendRawTransaction(rawTransaction).send();
        if (response.getResult() == null && response.getError() != null) {
            handleErrorUtils.handResponseError(response);
        } else if (response.getResult() == null && response.getError() == null) {
            throw new RuntimeException("Unkown error ");
        }

        return response.getTransactionHash();
    }

    @Override
    public org.web3j.protocol.core.methods.response.EthTransaction queryTransaction(String hash) throws Exception {
        EthTransaction response = null;

        response = fullNodeCache.getWeb3j().ethGetTransactionByHash(hash).send();

        if (response.getResult() == null && response.getError() != null) {
            if (response.getError().getCode() == -32602) {
                log.warn("queryTransaction :transaction not exist, error messages:{}", response.getError().getMessage());
                return null;
            }
            handleErrorUtils.handResponseError(response);
        } else if (response.getResult() == null && response.getError() == null) {
//            throw new Exception("transaction not exist");
            log.warn("queryTransaction :transaction not exist");
            return null;
        }
        return response;
    }

    @Override
    public TransactionReceipt queryTransactionReceipts(String hash) throws Exception {
        EthGetTransactionReceipt response = null;

        response = fullNodeCache.getWeb3j().ethGetTransactionReceipt(hash).send();

        if (response.getResult() == null && response.getError() != null) {
            handleErrorUtils.handResponseError(response);
        } else if (response.getResult() == null && response.getError() == null) {
//            throw new Exception("transaction not exist");
            log.warn("queryTransaction receipts :transaction not exist");
            return null;
        }
        return response.getResult();
    }

    @Override
    public BigInteger queryAddressNounce(String address) throws Exception {
        EthGetTransactionCount response = null;
        response = fullNodeCache.getWeb3j().ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();

        if (response.getResult() == null && response.getError() != null) {
            handleErrorUtils.handResponseError(response);
        } else if (response.getResult() == null && response.getError() == null) {
            throw new Exception("address not exist");
        }
        return response.getTransactionCount();
    }

    private String splitOutValue(String data) {
        if (data == null) {
            return "";
        }
        for (int index = 0; index < data.length(); index++) {
            if ((data.charAt(index) > '0' && data.charAt(index) <= '9')
                    || (data.charAt(index) >= 'a' && data.charAt(index) <= 'f')
                    || (data.charAt(index) >= 'A' && data.charAt(index) <= 'F')) {
                return data.substring(index, data.length());
            }
        }
        return "";
    }

    private boolean isSpecial(String data) throws Exception {
        if (ETH_TRUE_RETURN.equalsIgnoreCase(data)) {
            return true;
        } else if (ETH_FALUSE_RETURN.equalsIgnoreCase(data)) {
            return false;
        } else {
            throw new Exception("contract exception");
        }
    }
}
