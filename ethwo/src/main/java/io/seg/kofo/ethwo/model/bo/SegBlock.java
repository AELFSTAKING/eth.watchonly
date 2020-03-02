package io.seg.kofo.ethwo.model.bo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.seg.kofo.ethwo.common.util.Numeric;
import lombok.Data;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author WalkerLiu
 * @date 2018/9/18
 */

@Data
public class SegBlock {

    private String jsonrpc;
    private String id;
    private Block result;
    @Data
    public static class Block {
            private String number;
            private String hash;
            private String parentHash;
            private String nonce;
            private String sha3Uncles;
            private String logsBloom;
            private String transactionsRoot;
            private String stateRoot;
            private String receiptsRoot;
            private String author;
            private String miner;
            private String mixHash;
            private String difficulty;
            private String totalDifficulty;
            private String extraData;
            private String size;
            private String gasLimit;
            private String gasUsed;
            private String timestamp;
            private List<TransactionInfo> transactions;
            private List<String> uncles;
            private List<String> sealFields;

            public BigInteger getNumber() {
                return Numeric.decodeQuantity(number);
            }


            public void setNumber(String number) {
                this.number = number;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public String getParentHash() {
                return parentHash;
            }

            public void setParentHash(String parentHash) {
                this.parentHash = parentHash;
            }

            public BigInteger getNonce() {
                return Numeric.decodeQuantity(nonce);
            }


            public void setNonce(String nonce) {
                this.nonce = nonce;
            }

            public String getSha3Uncles() {
                return sha3Uncles;
            }

            public void setSha3Uncles(String sha3Uncles) {
                this.sha3Uncles = sha3Uncles;
            }

            public String getLogsBloom() {
                return logsBloom;
            }

            public void setLogsBloom(String logsBloom) {
                this.logsBloom = logsBloom;
            }

            public String getTransactionsRoot() {
                return transactionsRoot;
            }

            public void setTransactionsRoot(String transactionsRoot) {
                this.transactionsRoot = transactionsRoot;
            }

            public String getStateRoot() {
                return stateRoot;
            }

            public void setStateRoot(String stateRoot) {
                this.stateRoot = stateRoot;
            }

            public String getReceiptsRoot() {
                return receiptsRoot;
            }

            public void setReceiptsRoot(String receiptsRoot) {
                this.receiptsRoot = receiptsRoot;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getMiner() {
                return miner;
            }

            public void setMiner(String miner) {
                this.miner = miner;
            }

            public String getMixHash() {
                return mixHash;
            }

            public void setMixHash(String mixHash) {
                this.mixHash = mixHash;
            }

            public BigInteger getDifficulty() {
                return Numeric.decodeQuantity(difficulty);
            }


            public void setDifficulty(String difficulty) {
                this.difficulty = difficulty;
            }

            public BigInteger getTotalDifficulty() {
                return Numeric.decodeQuantity(totalDifficulty);
            }


            public void setTotalDifficulty(String totalDifficulty) {
                this.totalDifficulty = totalDifficulty;
            }

            public String getExtraData() {
                return extraData;
            }

            public void setExtraData(String extraData) {
                this.extraData = extraData;
            }

            public BigInteger getSize() {
                return Numeric.decodeQuantity(size);
            }


            public void setSize(String size) {
                this.size = size;
            }

            public BigInteger getGasLimit() {
                return Numeric.decodeQuantity(gasLimit);
            }


            public void setGasLimit(String gasLimit) {
                this.gasLimit = gasLimit;
            }

            public BigInteger getGasUsed() {
                return Numeric.decodeQuantity(gasUsed);
            }


            public void setGasUsed(String gasUsed) {
                this.gasUsed = gasUsed;
            }

            public BigInteger getTimestamp() {
                return Numeric.decodeQuantity(timestamp);
            }


            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public List<TransactionInfo> getTransactions() {
                return transactions;
            }

            @JsonDeserialize(using = SegBlock.ResultTransactionDeserialiser.class)
            public void setTransactions(List<TransactionInfo> transactions) {
                this.transactions = transactions;
            }

            public List<String> getUncles() {
                return uncles;
            }

            public void setUncles(List<String> uncles) {
                this.uncles = uncles;
            }

            public List<String> getSealFields() {
                return sealFields;
            }

            public void setSealFields(List<String> sealFields) {
                this.sealFields = sealFields;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof EthBlock.Block)) {
                    return false;
                }

                EthBlock.Block block = (EthBlock.Block) o;

                if (getNumber() != null
                        ? !getNumber().equals(block.getNumber()) : block.getNumber() != null) {
                    return false;
                }
                if (getHash() != null ? !getHash().equals(block.getHash()) : block.getHash() != null) {
                    return false;
                }
                if (getParentHash() != null
                        ? !getParentHash().equals(block.getParentHash())
                        : block.getParentHash() != null) {
                    return false;
                }
                if (getNonce() != null
                        ? !getNonce().equals(block.getNonce()) : block.getNonce() != null) {
                    return false;
                }
                if (getSha3Uncles() != null
                        ? !getSha3Uncles().equals(block.getSha3Uncles())
                        : block.getSha3Uncles() != null) {
                    return false;
                }
                if (getLogsBloom() != null
                        ? !getLogsBloom().equals(block.getLogsBloom())
                        : block.getLogsBloom() != null) {
                    return false;
                }
                if (getTransactionsRoot() != null
                        ? !getTransactionsRoot().equals(block.getTransactionsRoot())
                        : block.getTransactionsRoot() != null) {
                    return false;
                }
                if (getStateRoot() != null
                        ? !getStateRoot().equals(block.getStateRoot())
                        : block.getStateRoot() != null) {
                    return false;
                }
                if (getReceiptsRoot() != null
                        ? !getReceiptsRoot().equals(block.getReceiptsRoot())
                        : block.getReceiptsRoot() != null) {
                    return false;
                }
                if (getAuthor() != null
                        ? !getAuthor().equals(block.getAuthor()) : block.getAuthor() != null) {
                    return false;
                }
                if (getMiner() != null
                        ? !getMiner().equals(block.getMiner()) : block.getMiner() != null) {
                    return false;
                }
                if (getMixHash() != null
                        ? !getMixHash().equals(block.getMixHash()) : block.getMixHash() != null) {
                    return false;
                }
                if (getDifficulty() != null
                        ? !getDifficulty().equals(block.getDifficulty())
                        : block.getDifficulty() != null) {
                    return false;
                }
                if (getTotalDifficulty() != null
                        ? !getTotalDifficulty().equals(block.getTotalDifficulty())
                        : block.getTotalDifficulty() != null) {
                    return false;
                }
                if (getExtraData() != null
                        ? !getExtraData().equals(block.getExtraData())
                        : block.getExtraData() != null) {
                    return false;
                }
                if (getSize() != null
                        ? !getSize().equals(block.getSize())
                        : block.getSize() != null) {
                    return false;
                }
                if (getGasLimit() != null
                        ? !getGasLimit().equals(block.getGasLimit())
                        : block.getGasLimit() != null) {
                    return false;
                }
                if (getGasUsed() != null
                        ? !getGasUsed().equals(block.getGasUsed())
                        : block.getGasUsed() != null) {
                    return false;
                }
                if (getTimestamp() != null
                        ? !getTimestamp().equals(block.getTimestamp())
                        : block.getTimestamp() != null) {
                    return false;
                }
                if (getTransactions() != null
                        ? !getTransactions().equals(block.getTransactions())
                        : block.getTransactions() != null) {
                    return false;
                }
                if (getUncles() != null
                        ? !getUncles().equals(block.getUncles()) : block.getUncles() != null) {
                    return false;
                }
                return getSealFields() != null
                        ? getSealFields().equals(block.getSealFields()) : block.getSealFields() == null;
            }

            @Override
            public int hashCode() {
                int result = getNumber() != null ? getNumber().hashCode() : 0;
                result = 31 * result + (getHash() != null ? getHash().hashCode() : 0);
                result = 31 * result + (getParentHash() != null ? getParentHash().hashCode() : 0);
                result = 31 * result + (getNonce() != null ? getNonce().hashCode() : 0);
                result = 31 * result + (getSha3Uncles() != null ? getSha3Uncles().hashCode() : 0);
                result = 31 * result + (getLogsBloom() != null ? getLogsBloom().hashCode() : 0);
                result = 31 * result
                        + (getTransactionsRoot() != null ? getTransactionsRoot().hashCode() : 0);
                result = 31 * result + (getStateRoot() != null ? getStateRoot().hashCode() : 0);
                result = 31 * result + (getReceiptsRoot() != null ? getReceiptsRoot().hashCode() : 0);
                result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
                result = 31 * result + (getMiner() != null ? getMiner().hashCode() : 0);
                result = 31 * result + (getMixHash() != null ? getMixHash().hashCode() : 0);
                result = 31 * result + (getDifficulty() != null ? getDifficulty().hashCode() : 0);
                result = 31 * result
                        + (getTotalDifficulty() != null ? getTotalDifficulty().hashCode() : 0);
                result = 31 * result + (getExtraData() != null ? getExtraData().hashCode() : 0);
                result = 31 * result + (getSize() != null ? getSize().hashCode() : 0);
                result = 31 * result + (getGasLimit() != null ? getGasLimit().hashCode() : 0);
                result = 31 * result + (getGasUsed() != null ? getGasUsed().hashCode() : 0);
                result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
                result = 31 * result + (getTransactions() != null ? getTransactions().hashCode() : 0);
                result = 31 * result + (getUncles() != null ? getUncles().hashCode() : 0);
                result = 31 * result + (getSealFields() != null ? getSealFields().hashCode() : 0);
                return result;
            }

        @Override
        public String toString() {
            return "Block{" +
                    "number='" + number + '\'' +
                    ", hash='" + hash + '\'' +
                    ", parentHash='" + parentHash + '\'' +
                    ", nonce='" + nonce + '\'' +
                    ", sha3Uncles='" + sha3Uncles + '\'' +
                    ", logsBloom='" + logsBloom + '\'' +
                    ", transactionsRoot='" + transactionsRoot + '\'' +
                    ", stateRoot='" + stateRoot + '\'' +
                    ", receiptsRoot='" + receiptsRoot + '\'' +
                    ", author='" + author + '\'' +
                    ", miner='" + miner + '\'' +
                    ", mixHash='" + mixHash + '\'' +
                    ", difficulty='" + difficulty + '\'' +
                    ", totalDifficulty='" + totalDifficulty + '\'' +
                    ", extraData='" + extraData + '\'' +
                    ", size='" + size + '\'' +
                    ", gasLimit='" + gasLimit + '\'' +
                    ", gasUsed='" + gasUsed + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", transactions=" + transactions +
                    ", uncles=" + uncles +
                    ", sealFields=" + sealFields +
                    '}';
        }
    }


    public static class TransactionObject extends Transaction
            implements EthBlock.TransactionResult<Transaction> {
        public TransactionObject() {
        }

        public TransactionObject(String hash, String nonce, String blockHash, String blockNumber,
                                 String transactionIndex, String from, String to, String value,
                                 String gasPrice, String gas, String input, String creates,
                                 String publicKey, String raw, String r, String s, int v) {
            super(hash, nonce, blockHash, blockNumber, transactionIndex, from, to, value,
                    gasPrice, gas, input, creates, publicKey, raw, r, s, v);
        }

        @Override
        public Transaction get() {
            return this;
        }
    }

    public static class ResultTransactionDeserialiser
            extends JsonDeserializer<List<TransactionInfo>> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public List<TransactionInfo> deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {

            List<TransactionInfo> transactionResults = new ArrayList<>();
            JsonToken nextToken = jsonParser.nextToken();

            if (nextToken == JsonToken.START_OBJECT) {
                Iterator<TransactionInfo> transactionObjectIterator =
                        objectReader.readValues(jsonParser, TransactionInfo.class);
                while (transactionObjectIterator.hasNext()) {
                    transactionResults.add(transactionObjectIterator.next());
                }
            } else if (nextToken == JsonToken.VALUE_STRING) {
                jsonParser.getValueAsString();

                Iterator<TransactionInfo> transactionHashIterator =
                        objectReader.readValues(jsonParser, TransactionInfo.class);
                while (transactionHashIterator.hasNext()) {
                    transactionResults.add(transactionHashIterator.next());
                }
            }
            return transactionResults;
        }
    }

    public static class ResponseDeserialiser extends JsonDeserializer<Block> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public SegBlock.Block deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, SegBlock.Block.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
