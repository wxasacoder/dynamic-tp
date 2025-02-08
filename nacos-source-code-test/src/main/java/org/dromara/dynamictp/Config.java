package org.dromara.dynamictp;

import org.dromara.dynamictp.core.support.DynamicTp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxin
 * @date 2025/02/08 23:39:20
 */
@Configuration
public class Config {

    /**
     * ImportBeanDefinitionRegistrar 注册BeanDefinition 的时机要比@Bean早些时候，所以在Nacos上配置可名字与之相同的ThreadPool
     * 则会使用dynamic-tp创建的
     * * @return
     */
    @Bean
    @DynamicTp("myCustomExecutor")
    public Executor myCustomExecutor() {
        return new ThreadPoolExecutor(1,2, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }
}
