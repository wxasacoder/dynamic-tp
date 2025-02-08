/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.dynamictp.starter.cloud.nacos.refresher;

import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.common.properties.DtpProperties;
import org.dromara.dynamictp.spring.AbstractSpringRefresher;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.lang.NonNull;

/**
 * CloudNacosRefresher related
 *
 * @author yanhom
 * @since 1.0.0
 **/
@Slf4j
public class CloudNacosRefresher extends AbstractSpringRefresher implements SmartApplicationListener {

    public CloudNacosRefresher(DtpProperties dtpProperties) {
        super(dtpProperties);
    }

    @Override
    public boolean supportsEventType(@NonNull Class<? extends ApplicationEvent> eventType) {
        return EnvironmentChangeEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        /**
         * ContextRefresher 这个 监听器会 监听 RefreshEvent
         * 然后将传递过来的 新的env 和 内存中的env 进行对比
         * 随后 对比出来发生的变化的key，随后发送 EnvironmentChangeEvent dy-tp监听次 event 随后进行后续的动作
         *
         *
         * 附注：
         * nacos 能够实现刷新的原理的是，在引入 nacos 的config的stater的引用的时候，会引入 NacosConfigAutoConfiguration 在这个配置之中会引入 NacosContextRefresher
         * 这个 refresher 是一个监听器，他会监听容器准备完毕事件， 随后注册一个 com.alibaba.nacos.api.config.listener.Listener
         * 随后当这个 listener 被调用的时候，会发出一个 RefreshEvent 后续的流程就是上面的运行过程
         *
         * 而 Nacos 的更新模式是poll模式，默认是 5 sec 一次去拉取新的配置，如果有则调用 com.alibaba.nacos.api.config.listener.Listener
         * NacosConfigService 在被初始化的时候 会 new 一个 ClientWorker 这个随后就会使用丢一个周期性的 间隔 5秒的任务到周期性线程池中，进行周期行的检查config是否在被更新
         * new NacosConfigManager -> new NacosConfigService -> new ClientWorker -> start startInternal()
         */
        if (needRefresh(((EnvironmentChangeEvent) event).getKeys())) { // 在此过滤dy-tp关心的的key
            refresh(environment);
        }
    }
}
