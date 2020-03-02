package io.seg.kofo.api.response;

import lombok.*;

/**
 * @author: ZhuYuanxiang
 * @create: 2019-04-10 14:09
 */
@Data
public class QueryTransactionResponse {
    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;
    private String hash;
    private String nonce;
    private String blockHash;
    private String blockNumber;
    private String transactionIndex;
    private String from;
    private String to;
    private String value;
    private String gasPrice;
    private String gas;
    private String input;
    private String creates;
    private String publicKey;
    private String raw;
    private String r;
    private String s;
    private int v;
}
