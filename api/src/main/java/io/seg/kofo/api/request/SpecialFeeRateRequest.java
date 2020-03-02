package io.seg.kofo.api.request;

import lombok.Data;

/**
 * @author: ZhuYuanxiang
 * @create: 2019-03-11 15:51
 */
@Data
public class SpecialFeeRateRequest {

    /**
     * 分级用户的地址
     */
    private String specialAddress;

    /**
     * 合约地址
     */
    private String contractAddress;
}