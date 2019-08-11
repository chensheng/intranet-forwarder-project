package com.tamboot.intranetforwarder.common.message.body;

import space.chensheng.wsmessenger.message.component.MessageBody;
import space.chensheng.wsmessenger.message.component.MessageOptions;

public class HttpResponseMessageBody extends MessageBody {
    @MessageOptions(order = 0, notNull = false)
    private String requestId;

    @MessageOptions(order = 0, notNull = false)
    private int statusCode = 200;

    @MessageOptions(order = 1, notNull = false)
    private String headers;

    @MessageOptions(order = 2, notNull = false)
    private String body;

    public HttpResponseMessageBody() {
    }

    public HttpResponseMessageBody(String requestId, int statusCode, String headers, String body) {
        this.requestId = requestId;
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
