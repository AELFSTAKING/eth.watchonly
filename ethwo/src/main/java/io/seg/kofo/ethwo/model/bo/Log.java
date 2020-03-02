package io.seg.kofo.ethwo.model.bo;


import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * @author WalkerLiu
 * @date 2018/9/26
 */
public class Log {
    private boolean removed;
    private String logIndex;
    private String transactionIndex;
    private String transactionHash;
    private String blockHash;
    private String blockNumber;
    private String address;
    private String data;
    private String type;
    private List<String> topics;

    public Log() {
    }

    public Log(boolean removed, String logIndex, String transactionIndex, String transactionHash,
               String blockHash, String blockNumber, String address, String data, String type,
               List<String> topics) {
        this.removed = removed;
        this.logIndex = logIndex;
        this.transactionIndex = transactionIndex;
        this.transactionHash = transactionHash;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.address = address;
        this.data = data;
        this.type = type;
        this.topics = topics;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public BigInteger getLogIndex() {
        return convert(logIndex);
    }


    public void setLogIndex(String logIndex) {
        this.logIndex = logIndex;
    }

    public BigInteger getTransactionIndex() {
        return convert(transactionIndex);
    }


    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public BigInteger getBlockNumber() {
        return convert(blockNumber);
    }


    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    private BigInteger convert(String value) {
        if (value != null) {
            BigInteger result;
            if(value.startsWith("0x")){
                result = Numeric.decodeQuantity(value);
            }else {
                result = new BigInteger(value,10);
            }
            return result;
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof org.web3j.protocol.core.methods.response.Log)) {
            return false;
        }

        org.web3j.protocol.core.methods.response.Log log = (org.web3j.protocol.core.methods.response.Log) o;

        if (isRemoved() != log.isRemoved()) {
            return false;
        }
        if (getLogIndex() != null
                ? !getLogIndex().equals(log.getLogIndex()) : log.getLogIndex() != null) {
            return false;
        }
        if (getTransactionIndex() != null
                ? !getTransactionIndex().equals(log.getTransactionIndex())
                : log.getTransactionIndex() != null) {
            return false;
        }
        if (getTransactionHash() != null
                ? !getTransactionHash().equals(log.getTransactionHash())
                : log.getTransactionHash() != null) {
            return false;
        }
        if (getBlockHash() != null
                ? !getBlockHash().equals(log.getBlockHash()) : log.getBlockHash() != null) {
            return false;
        }
        if (getBlockNumber() != null
                ? !getBlockNumber().equals(log.getBlockNumber())
                : log.getBlockNumber() != null) {
            return false;
        }
        if (getAddress() != null
                ? !getAddress().equals(log.getAddress()) : log.getAddress() != null) {
            return false;
        }
        if (getData() != null ? !getData().equals(log.getData()) : log.getData() != null) {
            return false;
        }
        if (getType() != null ? !getType().equals(log.getType()) : log.getType() != null) {
            return false;
        }
        return getTopics() != null ? getTopics().equals(log.getTopics()) : log.getTopics() == null;
    }

    @Override
    public int hashCode() {
        int result = (isRemoved() ? 1 : 0);
        result = 31 * result + (getLogIndex() != null ? getLogIndex().hashCode() : 0);
        result = 31 * result
                + (getTransactionIndex() != null ? getTransactionIndex().hashCode() : 0);
        result = 31 * result + (getTransactionHash() != null ? getTransactionHash().hashCode() : 0);
        result = 31 * result + (getBlockHash() != null ? getBlockHash().hashCode() : 0);
        result = 31 * result + (getBlockNumber() != null ? getBlockNumber().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getTopics() != null ? getTopics().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Log{"
                + "removed=" + removed
                + ", logIndex='" + logIndex + '\''
                + ", transactionIndex='" + transactionIndex + '\''
                + ", transactionHash='" + transactionHash + '\''
                + ", blockHash='" + blockHash + '\''
                + ", blockNumber='" + blockNumber + '\''
                + ", address='" + address + '\''
                + ", data='" + data + '\''
                + ", type='" + type + '\''
                + ", topics=" + topics
                + '}';
    }
}
