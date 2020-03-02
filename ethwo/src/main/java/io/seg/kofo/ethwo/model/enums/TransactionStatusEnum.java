package io.seg.kofo.ethwo.model.enums;

/**
 * @author WalkerLiu
 * @date 2018/9/18
 */
public enum TransactionStatusEnum {
    SUCCESS(0,"SUCCESS"),
    FAILED(1,"FAILED");
    private Integer code;
    private String desc;

    TransactionStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

}
