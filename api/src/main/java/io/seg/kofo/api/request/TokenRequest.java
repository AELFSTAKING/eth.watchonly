package io.seg.kofo.api.request;

import lombok.Data;


/**
 * @author zhuyuanxiang
 */
@Data
public class TokenRequest {
    /**
     * erc20合约地址
     */
    private String tokenAddress;
}
