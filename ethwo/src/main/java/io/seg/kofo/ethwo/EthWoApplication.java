package io.seg.kofo.ethwo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableApolloConfig
@EnableFeignClients(basePackages = {"io.seg"})
@EnableScheduling
@Slf4j

/**
 * @author gin
 */
public class EthWoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EthWoApplication.class, args);
        log.info("{} is running...", EthWoApplication.class.getName());
    }
}

