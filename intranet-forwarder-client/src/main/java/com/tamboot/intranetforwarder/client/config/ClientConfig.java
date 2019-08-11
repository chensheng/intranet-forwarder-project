package com.tamboot.intranetforwarder.client.config;

import com.tamboot.common.tools.base.BeanUtil;
import com.tamboot.intranetforwarder.client.http.PoolingHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.chensheng.wsmessenger.client.ClientBootstrap;
import space.chensheng.wsmessenger.client.WsMessengerClient;
import space.chensheng.wsmessenger.client.component.ClientContext;
import space.chensheng.wsmessenger.client.component.ClientContextCustomizer;
import space.chensheng.wsmessenger.client.listener.ClientMessageListener;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ClientProperties.class)
public class ClientConfig {
    @Autowired(required = false)
    private List<ClientMessageListener<?>> msgListeners;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WsMessengerClient wsMessengerClient(ClientProperties properties) {
        return new ClientBootstrap()
                .setClientContextCustomizer(new ClientContextCustomizer() {
                    @Override
                    public void customize(ClientContext clientContext) {
                        BeanUtil.copyNotNullProperties(clientContext, properties);
                    }
                })
                .addMessageListeners(msgListeners)
                .build();
    }

    @Bean
    public PoolingHttpClient poolingHttpClient(ClientProperties properties) {
        return new PoolingHttpClient(properties);
    }
}
