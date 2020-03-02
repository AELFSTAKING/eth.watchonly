package io.seg.kofo.api.response;

import lombok.Data;

/**
 * @author WalkerLiu
 * @date 2018/9/21
 */
@Data
public class BalanceResponse {
    /**
     * 余额，hex format
     */
    String balance;

    /**
     * 对应的地址
     */
    String address;

    /**
     * 如果为空则为 ETH余额
     */
    String tokenAddress;

    /**
     * token name
     */
    String tokenName;
}
