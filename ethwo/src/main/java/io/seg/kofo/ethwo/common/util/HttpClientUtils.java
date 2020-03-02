package io.seg.kofo.ethwo.common.util;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author WalkerLiu
 * @date 2018/9/26
 */
@Slf4j
public class HttpClientUtils {
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(25,10, TimeUnit.SECONDS))
            .build();


    public static void main(String[] args) throws Exception {
       int i = 2000;
       while (i>0){
           MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
           RequestBody requestBody = RequestBody.create(jsonType, "{}");
           Request request = new Request.Builder().url("http://192.168.50.143:8545")
                   .post(requestBody)
                   .build();
           Response response = okHttpClient.newCall(request).execute();
           response.body().byteStream().close();
           System.out.println(2000 - i);
           i--;
       }
    }

    public static <T> T postMsg(String url, String msg, Class<T> rClass) {
        Response response = null;
        try {
            MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(jsonType, msg);
            Request request = new Request.Builder().url(url)
                    .post(requestBody)
                    .build();
            response = okHttpClient.newCall(request).execute();
            if (response.code() != 200) {
                log.error("response status code != 200 right  {}", response.code());
            }
            String body = IOUtils.toString(response.body().byteStream(), "UTF-8");
            return JSON.parseObject(body, rClass);
        } catch (Exception e) {
            log.error("post request msg error : {}", e.getMessage(),e);
        }finally {
            if (Objects.nonNull(response)){
                response.close();
            }
        }
        return null;
    }
}
