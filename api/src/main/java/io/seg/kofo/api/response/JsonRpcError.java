package io.seg.kofo.api.response;

import lombok.Data;

@Data
public class JsonRpcError {
    String code;
    String message;
}
