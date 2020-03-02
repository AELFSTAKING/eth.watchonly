package io.seg.kofo.api.response;

import lombok.Data;

/**
 * @author: ZhuYuanxiang
 * @create: 2019-03-11 15:58
 */
@Data
public class SpecialFeeRateResponse {

    /**
     * 判断是否是分级用户的地址
     */
    boolean isSpecial;
    /**
     * 分级手续费率
     */
    String specialFeeRate;
}