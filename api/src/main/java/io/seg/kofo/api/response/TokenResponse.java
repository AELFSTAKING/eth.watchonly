package io.seg.kofo.api.response;

import lombok.Data;


/**
 * @author zhuyuanxiang
 */
@Data
public class TokenResponse {
    /**
     * 发行量
     */
    String totalSupply;

    /**
     * 小数位数
     */
    String decimals;

    /**
     * erc20合约地址
     */
    String tokenAddress;

    /**
     * token name
     */
    String name;
}
