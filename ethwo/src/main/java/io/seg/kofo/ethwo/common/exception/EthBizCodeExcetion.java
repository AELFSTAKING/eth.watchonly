package io.seg.kofo.ethwo.common.exception;

import io.seg.kofo.common.exception.BizErrorCode;

public class EthBizCodeExcetion extends RuntimeException implements BizErrorCode {


    private Long code;
    private String msg;

    public EthBizCodeExcetion(Long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return String.valueOf(code);
    }

    @Override
    public String getDescription() {
        return msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
