package com.tamboot.intranetforwarder.common.message;

import com.tamboot.intranetforwarder.common.message.body.HttpRequestMessageBody;
import space.chensheng.wsmessenger.message.component.WsMessage;

public class HttpRequestMessage extends WsMessage<HttpRequestMessageBody> {
    public HttpRequestMessage() {
        this(null, null, null, null, null, null);
    }

    public HttpRequestMessage(String requestId, String url, String method, String headers, String queryString, String body) {
        super(new HttpRequestMessageBody(requestId, url, method, headers, queryString, body));
    }
}
