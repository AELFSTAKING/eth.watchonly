package io.seg.kofo.ethwo.model.bo;

import lombok.Data;

/**
 * gethRpcClient使用
 * @author gin
 */
@Data
public class EthBlockCount {
    private String jsonrpc;
    private String id;
    private String result;
}
