package io.seg.kofo.ethwo.model.bo;

import lombok.Data;

/**
 * @author WalkerLiu
 * @date 2018/9/18
 */
@Data
public class EtherTransfer {
    private String txHash;
    private String type;
    private int depth;
    private String from;
    private String to;
    private String value;
    private int index;
    private String contractAddress;
    private int isError;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EtherTransfer)) return false;

        EtherTransfer that = (EtherTransfer) o;

        if (!txHash.equals(that.txHash)) return false;
        if (!type.equals(that.type)) return false;
        if (!from.equals(that.from)) return false;
        if (!to.equals(that.to)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = txHash.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
