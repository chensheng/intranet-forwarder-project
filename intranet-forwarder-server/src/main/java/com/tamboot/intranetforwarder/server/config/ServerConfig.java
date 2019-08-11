package com.tamboot.intranetforwarder.server.config;

import com.tamboot.common.tools.base.BeanUtil;
import com.tamboot.intranetforwarder.server.core.HttpResponseMessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.chensheng.wsmessenger.server.ServerBootstrap;
import space.chensheng.wsmessenger.server.WsMessengerServer;
import space.chensheng.wsmessenger.server.component.ServerContext;
import space.chensheng.wsmessenger.server.component.ServerContextCustomizer;
import space.chensheng.wsmessenger.server.listener.ServerMessageListener;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ServerProperties.class)
public class ServerConfig {
    @Autowired(required = false)
    private List<ServerMessageListener<?>> msgListeners;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WsMessengerServer wsMessengerServer(final ServerProperties properties) {
        return new ServerBootstrap()
                .setServerContextCustomizer(new ServerContextCustomizer() {
                    public void customize(ServerContext serverContext) {
                        BeanUtil.copyNotNullProperties(serverContext, properties);
                    }
                })
                .addMessageListeners(msgListeners)
                .build();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public HttpResponseMessageStore httpResponseMessageStore(ServerProperties properties) {
        return new HttpResponseMessageStore(properties);
    }
}
