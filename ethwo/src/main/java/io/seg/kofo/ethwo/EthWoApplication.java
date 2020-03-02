package io.seg.kofo.ethwo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import io.seg.frame.job.starter.annotation.EnableJob;
import io.seg.framework.sequence.sdk.annotation.EnableSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableApolloConfig
@EnableJob
@EnableSequence
@EnableFeignClients(basePackages = {"io.seg", "io.seg.framework.sequence.sdk.client"})
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

