package com.tamboot.intranetforwarder.common.message.body;

import space.chensheng.wsmessenger.message.component.MessageBody;
import space.chensheng.wsmessenger.message.component.MessageOptions;

public class HttpRequestMessageBody extends MessageBody {
    @MessageOptions(order = 0, notNull = false)
    private String requestId;

    @MessageOptions(order = 1, notNull = false)
    private String url;

    @MessageOptions(order = 2, notNull = false)
    private String method;

    @MessageOptions(order = 3, notNull = false)
    private String headers;

    @MessageOptions(order = 4, notNull = false)
    private String queryString;

    @MessageOptions(order = 5, notNull = false)
    private String body;

    public HttpRequestMessageBody() {
    }

    public HttpRequestMessageBody(String requestId, String url, String method, String headers, String queryString, String body) {
        this.requestId = requestId;
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.queryString = queryString;
        this.body = body;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
