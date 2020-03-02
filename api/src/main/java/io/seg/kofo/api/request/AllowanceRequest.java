package io.seg.kofo.api.request;

import lombok.Data;


@Data
public class AllowanceRequest {
    /**
     * 合约地址，为空则为 ether的余额
     */
    private String tokenAddress;

    /**
     *
     */
    private String ownerAddress;

    /**
     *
     */
    private String spenderAddress;
}
