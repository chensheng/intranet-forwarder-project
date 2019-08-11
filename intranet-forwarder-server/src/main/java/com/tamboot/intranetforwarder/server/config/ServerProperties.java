package com.tamboot.intranetforwarder.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tamboot.intranet-forwarder.server")
public class ServerProperties {
    private String serverId = "wsmessenger-server";

    private int serverPort = 2046;

    private int pendingClientMaxCount = 100;

    private int pendingClientMaxMsg = 100;

    private int pendingClientTimeoutMillis = 3600000;

    private int pendingClientTimeoutCheckerIntervalMinutes = 5;

    private int acceptorThreadSize = 1;

    private int ioThreadSize = 4;

    private int heartbeatIntervalSeconds = 60;

    private int heartbeatMaxFail = 2;

    private int soBacklog = 3074;

    private boolean allowHalfClosure = false;

    private int maxContentLen = 1048576;

    private int maxFrameSize = 1048576;

    private int businessThreadSize = 8;

    private int retryTaskMaxSize = 1000;

    private int maxPendingMsg = 1000;

    private int waitingMsgTimeoutMillis = 120000;

    private int waitingMsgMaxSize = 1000;

    private long httpRequestTimeoutMillis = 60000;

    private long httpResponsePollIntervalMillis = 500;

    private Forward[] forwards;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getPendingClientMaxCount() {
        return pendingClientMaxCount;
    }

    public void setPendingClientMaxCount(int pendingClientMaxCount) {
        this.pendingClientMaxCount = pendingClientMaxCount;
    }

    public int getPendingClientMaxMsg() {
        return pendingClientMaxMsg;
    }

    public void setPendingClientMaxMsg(int pendingClientMaxMsg) {
        this.pendingClientMaxMsg = pendingClientMaxMsg;
    }

    public int getPendingClientTimeoutMillis() {
        return pendingClientTimeoutMillis;
    }

    public void setPendingClientTimeoutMillis(int pendingClientTimeoutMillis) {
        this.pendingClientTimeoutMillis = pendingClientTimeoutMillis;
    }

    public int getPendingClientTimeoutCheckerIntervalMinutes() {
        return pendingClientTimeoutCheckerIntervalMinutes;
    }

    public void setPendingClientTimeoutCheckerIntervalMinutes(int pendingClientTimeoutCheckerIntervalMinutes) {
        this.pendingClientTimeoutCheckerIntervalMinutes = pendingClientTimeoutCheckerIntervalMinutes;
    }

    public int getAcceptorThreadSize() {
        return acceptorThreadSize;
    }

    public void setAcceptorThreadSize(int acceptorThreadSize) {
        this.acceptorThreadSize = acceptorThreadSize;
    }

    public int getIoThreadSize() {
        return ioThreadSize;
    }

    public void setIoThreadSize(int ioThreadSize) {
        this.ioThreadSize = ioThreadSize;
    }

    public int getHeartbeatIntervalSeconds() {
        return heartbeatIntervalSeconds;
    }

    public void setHeartbeatIntervalSeconds(int heartbeatIntervalSeconds) {
        this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
    }

    public int getHeartbeatMaxFail() {
        return heartbeatMaxFail;
    }

    public void setHeartbeatMaxFail(int heartbeatMaxFail) {
        this.heartbeatMaxFail = heartbeatMaxFail;
    }

    public int getSoBacklog() {
        return soBacklog;
    }

    public void setSoBacklog(int soBacklog) {
        this.soBacklog = soBacklog;
    }

    public boolean isAllowHalfClosure() {
        return allowHalfClosure;
    }

    public void setAllowHalfClosure(boolean allowHalfClosure) {
        this.allowHalfClosure = allowHalfClosure;
    }

    public int getMaxContentLen() {
        return maxContentLen;
    }

    public void setMaxContentLen(int maxContentLen) {
        this.maxContentLen = maxContentLen;
    }

    public int getMaxFrameSize() {
        return maxFrameSize;
    }

    public void setMaxFrameSize(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    public int getBusinessThreadSize() {
        return businessThreadSize;
    }

    public void setBusinessThreadSize(int businessThreadSize) {
        this.businessThreadSize = businessThreadSize;
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

    public long getHttpRequestTimeoutMillis() {
        return httpRequestTimeoutMillis;
    }

    public void setHttpRequestTimeoutMillis(long httpRequestTimeoutMillis) {
        this.httpRequestTimeoutMillis = httpRequestTimeoutMillis;
    }

    public long getHttpResponsePollIntervalMillis() {
        return httpResponsePollIntervalMillis;
    }

    public void setHttpResponsePollIntervalMillis(long httpResponsePollIntervalMillis) {
        this.httpResponsePollIntervalMillis = httpResponsePollIntervalMillis;
    }

    public Forward[] getForwards() {
        return forwards;
    }

    public void setForwards(Forward[] forwards) {
        this.forwards = forwards;
    }

    public static class Forward {
        private String clientId;

        private String from;

        private String to;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}
