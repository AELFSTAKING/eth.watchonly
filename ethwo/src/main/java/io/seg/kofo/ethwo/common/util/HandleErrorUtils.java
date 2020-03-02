package io.seg.kofo.ethwo.common.util;

import com.alibaba.fastjson.JSON;
import io.seg.kofo.ethwo.common.exception.EthBizCodeExcetion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.Response;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author WalkerLiu
 * @date 2018/10/15
 */
@Component
@Slf4j
public class HandleErrorUtils {
    //read only after construct !!!
    private HashMap<String, Long> errCodeMap = new HashMap<>();

    @PostConstruct
    public void construct() {
        ErrorCode errorCode = new ErrorCode();
        try {
            for (int index = 0; index < ErrorCode.class.getFields().length; index ++) {
                Field codeFiled = ErrorCode.class.getFields()[index];
                codeFiled.setAccessible(true);
                ErrorTemplate errorTemplate = codeFiled.getAnnotation(ErrorTemplate.class);
                Long code = (Long)codeFiled.get(errorCode);
                errCodeMap.put(errorTemplate.msg(),code);
            }
            log.info("err code map :{}", JSON.toJSONString(errCodeMap));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    //todo 此处应该透传错误码
    public String handResponseError(Response response)  {
        int code = response.getError().getCode();
        String msg = response.getError().getMessage();
        log.info("error response  code:{}, message:{}",code,msg);
        throw new EthBizCodeExcetion(Long.valueOf(code+""),msg);
    }
}
