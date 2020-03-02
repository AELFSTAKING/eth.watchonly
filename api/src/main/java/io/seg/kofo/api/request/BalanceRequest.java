package io.seg.kofo.api.request;

import lombok.Data;

/**
 * @author WalkerLiu
 * @date 2018/9/21
 */
@Data
public class BalanceRequest {
    /**
     * 合约地址，为空则为 ether的余额
     */
    private String tokenAddress;

    /**
     * 指定的地址
     */
    private String address;
}
