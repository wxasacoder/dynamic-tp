package org.dromara.dynamictp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wuxin
 * @date 2025/02/07 21:54:42
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosTestApplication.class, args);
    }
}
