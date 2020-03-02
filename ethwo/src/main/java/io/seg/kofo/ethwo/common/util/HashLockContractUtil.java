package io.seg.kofo.ethwo.common.util;

import io.seg.kofo.ethwo.common.config.FullNodeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.util.Collections;

/**
 * @author: ZhuYuanxiang
 * @create: 2019-03-11 16:37
 */
@Component
public class HashLockContractUtil {

    @Autowired
    private FullNodeCache fullNodeCache;

    private static final String DEFAULT_FROM_ADDRESS = "0x0000000000000000000000000000000000000000";

    private String execute(Function function, String contractAddress) throws Exception {
        String encodedFunction = FunctionEncoder.encode(function);

        EthCall response = fullNodeCache.getWeb3j().ethCall(
                Transaction.createEthCallTransaction(
                        DEFAULT_FROM_ADDRESS, contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .sendAsync().get();
        if (response.getError() != null) {
            throw new Exception("error when call function : " + function.getName() + " : " + response.getError().getMessage());
        }
        return response.getValue();
    }

    public String queryFeeRate(String contractAddress) throws Exception{
        Function function = new Function(
                "getFeeRate",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return execute(function, contractAddress);
    }

    public String queryFeeAccount(String contractAddress) throws  Exception {
        Function function = new Function(
                "getFeeAccount",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Address>() {
                }));
        return execute(function, contractAddress);
    }

    public String queryAdminAccount(String contractAddress) throws  Exception {
        Function function = new Function(
                "getAdmin",
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Address>() {
                }));
        return execute(function, contractAddress);
    }

    public String querySpecialFeeRate(String contractAddress, String specialAddress) throws Exception{
        Function function = new Function(
                "getSpecialFeeRate",
                Collections.singletonList(new Address(specialAddress)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        return execute(function, contractAddress);
    }

    public String queryIsSpecialAddress(String contractAddress, String specialAddress) throws Exception {
        Function function = new Function(
                "isSpecialAddress",
                Collections.singletonList(new Address(specialAddress)),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        return execute(function, contractAddress);
    }
}