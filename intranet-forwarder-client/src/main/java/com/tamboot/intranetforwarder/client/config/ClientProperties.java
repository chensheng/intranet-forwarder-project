package com.tamboot.intranetforwarder.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tamboot.intranet-forwarder.client")
public class ClientProperties {
    private String clientId;

    private String serverUrl;

    private int ioThreadPoolSize = 1;

    private int heartbeatSeconds = 60;

    private int heartbeatMaxFail = 3;

    private int reconnectMillis = 10000;

    private int maxContentLen = 1048576;

    private int maxFramePayloadLen = 1048576;

    private int businessThreadPoolSize = 4;

    private int retryTaskMaxSize = 1000;

    private int maxPendingMsg = 1000;

    private int waitingMsgTimeoutMillis = 120000;

    private int waitingMsgMaxSize = 1000;

    private HttpClient httpclient = new HttpClient();

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public int getIoThreadPoolSize() {
        return ioThreadPoolSize;
    }

    public void setIoThreadPoolSize(int ioThreadPoolSize) {
        this.ioThreadPoolSize = ioThreadPoolSize;
    }

    public int getHeartbeatSeconds() {
        return heartbeatSeconds;
    }

    public void setHeartbeatSeconds(int heartbeatSeconds) {
        this.heartbeatSeconds = heartbeatSeconds;
    }

    public int getHeartbeatMaxFail() {
        return heartbeatMaxFail;
    }

    public void setHeartbeatMaxFail(int heartbeatMaxFail) {
        this.heartbeatMaxFail = heartbeatMaxFail;
    }

    public int getReconnectMillis() {
        return reconnectMillis;
    }

    public void setReconnectMillis(int reconnectMillis) {
        this.reconnectMillis = reconnectMillis;
    }

    public int getMaxContentLen() {
        return maxContentLen;
    }

    public void setMaxContentLen(int maxContentLen) {
        this.maxContentLen = maxContentLen;
    }

    public int getMaxFramePayloadLen() {
        return maxFramePayloadLen;
    }

    public void setMaxFramePayloadLen(int maxFramePayloadLen) {
        this.maxFramePayloadLen = maxFramePayloadLen;
    }

    public int getBusinessThreadPoolSize() {
        return businessThreadPoolSize;
    }

    public void setBusinessThreadPoolSize(int businessThreadPoolSize) {
        this.businessThreadPoolSize = businessThreadPoolSize;
    }

    public int getRetryTaskMaxSize() {
        return retryTaskMaxSize;
    }

    public void setRetryTaskMaxSize(int retryTaskMaxSize) {
        this.retryTaskMaxSize = retryTaskMaxSize;
    }

    public int getMaxPendingMsg() {
        return maxPendingMsg;
    }

    public void setMaxPendingMsg(int maxPendingMsg) {
        this.maxPendingMsg = maxPendingMsg;
    }

    public int getWaitingMsgTimeoutMillis() {
        return waitingMsgTimeoutMillis;
    }

    public void setWaitingMsgTimeoutMillis(int waitingMsgTimeoutMillis) {
        this.waitingMsgTimeoutMillis = waitingMsgTimeoutMillis;
    }

    public int getWaitingMsgMaxSize() {
        return waitingMsgMaxSize;
    }

    public void setWaitingMsgMaxSize(int waitingMsgMaxSize) {
        this.waitingMsgMaxSize = waitingMsgMaxSize;
    }

    public HttpClient getHttpclient() {
        return httpclient;
    }

    public void setHttpclient(HttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public static class HttpClient {
        private int maxConnTotal = 100;

        private int maxConnPerRoute = 50;

        private boolean tcpNoDelay = true;

        private int socketTimeoutMillis = 60000;

        private int connectTimeoutMillis = 30000;

        private int connectionRequestTimeoutMillis = 5000;

        public int getMaxConnTotal() {
            return maxConnTotal;
        }

        public void setMaxConnTotal(int maxConnTotal) {
            this.maxConnTotal = maxConnTotal;
        }

        public int getMaxConnPerRoute() {
            return maxConnPerRoute;
        }

        public void setMaxConnPerRoute(int maxConnPerRoute) {
            this.maxConnPerRoute = maxConnPerRoute;
        }

        public boolean isTcpNoDelay() {
            return tcpNoDelay;
        }

        public void setTcpNoDelay(boolean tcpNoDelay) {
            this.tcpNoDelay = tcpNoDelay;
        }

        public int getSocketTimeoutMillis() {
            return socketTimeoutMillis;
        }

        public void setSocketTimeoutMillis(int socketTimeoutMillis) {
            this.socketTimeoutMillis = socketTimeoutMillis;
        }

        public int getConnectTimeoutMillis() {
            return connectTimeoutMillis;
        }

        public void setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
        }

        public int getConnectionRequestTimeoutMillis() {
            return connectionRequestTimeoutMillis;
        }

        public void setConnectionRequestTimeoutMillis(int connectionRequestTimeoutMillis) {
            this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
        }
    }
}
