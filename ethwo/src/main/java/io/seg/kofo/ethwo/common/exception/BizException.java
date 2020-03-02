package io.seg.kofo.ethwo.common.exception;

import lombok.Data;

/**
 * 业务异常
 *
 * @author ShenTeng
 */
@Data
public class BizException extends RuntimeException {
    private Long code;
    private String msg;

    public BizException(Long code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
