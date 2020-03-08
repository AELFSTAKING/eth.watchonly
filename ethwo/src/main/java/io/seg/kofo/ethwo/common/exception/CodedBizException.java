package io.seg.kofo.ethwo.common.exception;


import io.seg.kofo.api.response.ResponseEnum;
import org.apache.commons.lang.StringUtils;

/**
 * 带RespCode的业务异常，可以自动转换成响应JSON
 *
 */
public class CodedBizException extends BizException {

    private ResponseEnum respCode;

    private String extraMsg;

    public CodedBizException(ResponseEnum respCode) {
        super(respCode.getDesc());
        this.respCode = respCode;
    }

    public CodedBizException(ResponseEnum respCode, String extraMsg) {
        super(StringUtils.defaultString(extraMsg));
        this.respCode = respCode;
        this.extraMsg = extraMsg;
    }

    public CodedBizException(ResponseEnum respCode, Throwable cause) {
        super(respCode.getDesc(), cause);
        this.respCode = respCode;
        //this.extraMsg = extraMsg;
    }

    public CodedBizException(ResponseEnum respCode, String extraMsg, Throwable cause) {
        super(StringUtils.defaultString(extraMsg), cause);
        this.respCode = respCode;
        this.extraMsg = extraMsg;
    }

    public ResponseEnum getRespCode() {
        return respCode;
    }

    public void setRespCode(ResponseEnum respCode) {
        this.respCode = respCode;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }

}
