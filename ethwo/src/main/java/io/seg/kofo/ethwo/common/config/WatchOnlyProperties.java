package io.seg.kofo.ethwo.common.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix = "watch-only")
@Data
@RefreshScope
public class WatchOnlyProperties {

    /**
     * 获取外网最高高度
     * https://api.etherscan.io/api?module=proxy&action=eth_blockNumber&apikey=YourApiKeyToken
     * https://api-rinkeby.etherscan.io/api?module=proxy&action=eth_blockNumber&apikey=YourApiKeyToken
     */
    private String latestBlockUrl;
    /**
     * rpc调用fullnode
     * 例如"192.168.50.143:8545,192.168.50.144:8545"
     */
    private String rpcNodes;
    /**
     * 消息队列长度
     */
    private String msgQueueLimit;

    /**
     * 允许的节点滞后数值
     */
    private int lagThreshold;

    /**
     * 临时配置 监控两个地址eth余额
     */
    private String tmpEthBalanceMonitorProduct;
    private String tmpEthBalanceMonitorPre;
    /**
     * 临时监控阈值
     */
    private String tmpEthBalanceProductThreshold;
    private String tmpEthBalancePreThreshold;


}
