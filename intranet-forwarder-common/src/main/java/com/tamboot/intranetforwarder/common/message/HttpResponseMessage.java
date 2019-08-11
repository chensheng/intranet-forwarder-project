package com.tamboot.intranetforwarder.common.message;

import com.tamboot.intranetforwarder.common.message.body.HttpResponseMessageBody;
import space.chensheng.wsmessenger.message.component.WsMessage;

public class HttpResponseMessage extends WsMessage<HttpResponseMessageBody> {
    public HttpResponseMessage() {
        this(null, 200, null, null);
    }

    public HttpResponseMessage(String requestId, int statusCode, String headers, String body) {
        super(new HttpResponseMessageBody(requestId, statusCode, headers, body));
    }
}
