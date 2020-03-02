package io.seg.kofo.ethwo.common.util;

import com.alibaba.fastjson.JSON;
import io.seg.kofo.ethwo.model.bo.ETHRequest;
import io.seg.kofo.ethwo.model.bo.EthBlockCount;
import io.seg.kofo.ethwo.model.bo.SegBlock;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;

import java.util.ArrayList;
import java.util.Arrays;
import static org.web3j.utils.Numeric.cleanHexPrefix;


@Slf4j
@Data
public class GethRpcClient {

    private String url;

    private static final String API_GET_BLOCK_BY_NUMBER = "eth_segGetBlockByNumber";
    private static final String API_GET_BLOCK_BY_HASH = "eth_segGetBlockByHash";
    private static final String API_GET_BLOCK_COUNT = "eth_blockNumber";

    public GethRpcClient(String url) {
        this.url = "http://" + url + "/";
    }

    public SegBlock.Block getBlockByNumber(long blockNumber){
        ETHRequest ethRequest = new ETHRequest();
        ethRequest.setId(System.currentTimeMillis() / 1000 + "");
        ethRequest.setMethod(API_GET_BLOCK_BY_NUMBER);
        DefaultBlockParameterNumber blockParameterNumber = new DefaultBlockParameterNumber(blockNumber);
        ethRequest.setParams(Arrays.asList(blockParameterNumber.getValue()));
        log.info("geth rpc url is {}, request is {}", url, JSON.toJSONString(ethRequest));
        SegBlock.Block block = HttpClientUtils.postMsg(url, JSON.toJSONString(ethRequest), SegBlock.class).getResult();
        return block;
    }


    public SegBlock.Block getBlockByHash(String blockHash){
        ETHRequest ethRequest = new ETHRequest();
        ethRequest.setId(System.currentTimeMillis() / 1000 + "");
        ethRequest.setMethod(API_GET_BLOCK_BY_HASH);
        ethRequest.setParams(Arrays.asList(blockHash));
        log.info("geth rpc url is {}, request is {}", url, JSON.toJSONString(ethRequest));
        SegBlock.Block block = HttpClientUtils.postMsg(url, JSON.toJSONString(ethRequest), SegBlock.class).getResult();
        return block;
    }

    public Long getBlockCount(){
        ETHRequest ethRequest = new ETHRequest();
        ethRequest.setId(System.currentTimeMillis() / 1000 + "");
        ethRequest.setMethod(API_GET_BLOCK_COUNT);
        ethRequest.setParams(new ArrayList<>());
        log.info("geth rpc url is {}, request is {}", url, JSON.toJSONString(ethRequest));
        String blockCountStr = HttpClientUtils.postMsg(url, JSON.toJSONString(ethRequest), EthBlockCount.class).getResult();
        return Long.parseLong(Numeric.toBigInt(blockCountStr).toString(),10);
    }

    public static void main(String[] args) {
        String result = "0x343c4c";
        long aa = Long.parseLong(Numeric.toBigInt(result).toString(),10);
        System.out.println(aa);
    }



}
