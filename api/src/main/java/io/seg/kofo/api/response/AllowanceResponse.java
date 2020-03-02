package io.seg.kofo.api.response;

import lombok.Data;

@Data
public class AllowanceResponse {
    /**
     * 余额，hex format
     */
    String balance;

    /**
     * 对应的地址
     */
    String ownerAddress;
    /**
     *
     */
    String spenderAddress;

    /**
     * 如果为空则为 ETH余额
     */
    String tokenAddress;

    /**
     * token name
     */
    String tokenName;
}
