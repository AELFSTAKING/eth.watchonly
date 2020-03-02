package io.seg.kofo.api.response;

import lombok.Data;
import org.web3j.protocol.core.methods.response.Log;

import java.util.List;

/**
 * @author: ZhuYuanxiang
 * @create: 2019-04-10 14:09
 */
@Data
public class QueryTransactionReceiptResponse {
    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;
    private String transactionHash;
    private String transactionIndex;
    private String blockHash;
    private String blockNumber;
    private String cumulativeGasUsed;
    private String gasUsed;
    private String contractAddress;
    private String root;
    private String status;
    private String from;
    private String to;
    private List<Log> logs;
    private String logsBloom;
}
