package io.seg.kofo.ethwo.model.bo;

import lombok.Data;

import java.util.List;

/**
 * @author WalkerLiu
 * @date 2018/9/28
 */
@Data
public class ETHRequest {
    private String jsonrpc = "2.0";
    private String id;
    private List<Object> params;
    private String method;
}
