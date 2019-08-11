package com.tamboot.intranetforwarder.server.listener;

import com.tamboot.intranetforwarder.common.message.HttpResponseMessage;
import com.tamboot.intranetforwarder.server.core.HttpResponseMessageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.chensheng.wsmessenger.server.MessengerServer;
import space.chensheng.wsmessenger.server.clientmng.ClientInfo;
import space.chensheng.wsmessenger.server.listener.ServerMessageListener;

@Component
public class HttpResponseMessageListener extends ServerMessageListener<HttpResponseMessage> {
    @Autowired
    private HttpResponseMessageStore httpResponseMessageStore;

    @Override
    protected void onMessage(HttpResponseMessage httpResponseMessage, ClientInfo clientInfo, MessengerServer messengerServer) {
        httpResponseMessageStore.put(httpResponseMessage);
    }
}
