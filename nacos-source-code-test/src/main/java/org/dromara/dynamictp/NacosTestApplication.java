package org.dromara.dynamictp;

import org.dromara.dynamictp.spring.annotation.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.SpringVersion;

/**
 * @author wuxin
 * @date 2025/02/07 21:54:42
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDynamicTp
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class NacosTestApplication {
    public static void main(String[] args) {
        System.err.println("SpringVersion："+ SpringVersion.getVersion() + "SpringBootVersion:" + SpringBootVersion.getVersion());
        ConfigurableApplicationContext run = SpringApplication.run(NacosTestApplication.class, args);
        System.err.println(run.getBean("dtpExecutor1").getClass().getSimpleName());
    }
}
