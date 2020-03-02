package io.seg.kofo.ethwo.common.util;

/**
 * @author WalkerLiu
 * @date 2018/9/21
 */

import io.seg.kofo.ethwo.common.config.FullNodeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

/**
 * help  build erc 20 call functions
 */
@Component
public class ERC20FunctionUtils {


    private Web3j web3j ;

    FullNodeCache fullNodeCache;

    public void setWeb3j(Web3j web3j) {
        this.web3j = web3j;
    }

    public ERC20FunctionUtils() {
    }

    @Autowired
    public ERC20FunctionUtils(FullNodeCache fullNodeCache) {
        this.fullNodeCache = fullNodeCache;
        this.web3j = fullNodeCache.getWeb3j();
    }


    private String execute(
            Function function, String contractAddress) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);

        //FIXME : fix account issue
        org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
                Transaction.createEthCallTransaction(
                        "0x0000000000000000000000000000000000000000", contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();
        if (response.getError() != null){
            throw new Exception("error when call function : " + function.getName() + " : " + response.getError().getMessage());
        }
        return response.getValue();
    }

    public String totalSupply(String contractAddress) throws Exception {
        Function function = new Function(
                "totalSupply",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return execute(function, contractAddress);
    }

    public String balanceOf(String contractAddress, String owner) throws Exception {
        Function function = new Function(
                "balanceOf",
                Collections.singletonList(new Address(owner)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return execute(function, contractAddress);
    }

    public String transfer(String contractAddress, String to, BigInteger value) throws Exception {
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(value)),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        return execute(function, contractAddress);
    }

    public String allowance(String contractAddress, String owner, String spender) throws Exception {
        Function function = new Function(
                "allowance",
                Arrays.asList(new Address(owner), new Address(spender)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return execute(function, contractAddress);
    }

    public String approve(String contractAddress, String spender, BigInteger value) throws Exception {
        Function function = new Function(
                "approve",
                Arrays.asList(new Address(spender), new Uint256(value)),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        return execute(function, contractAddress);
    }

    public String transferFrom(String contractAddress, String from, String to, BigInteger value) throws Exception {
        Function function = new Function(
                "transferFrom",
                Arrays.asList(new Address(from), new Address(to), new Uint256(value)),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        return execute(function, contractAddress);
    }

    public String getDecimal(String contractAddress) throws Exception {
        Function function = new Function(
                "decimals",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint8>() {
                }));
        return execute(function, contractAddress);
    }

    public String getName(String contractAddress) throws Exception {
        Function function = new Function(
                "name",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Utf8String>() {
                }));
        return execute(function, contractAddress);
    }
}
