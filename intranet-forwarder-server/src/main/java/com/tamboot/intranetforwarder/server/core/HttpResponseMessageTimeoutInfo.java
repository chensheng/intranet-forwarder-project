package com.tamboot.intranetforwarder.server.core;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpResponseMessageTimeoutInfo implements Delayed {
    private long trigger;

    private String requestId;

    HttpResponseMessageTimeoutInfo(String requestId, long timeoutMillis) {
        this.requestId = requestId;
        this.trigger = System.currentTimeMillis() + timeoutMillis;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public int compareTo(Delayed o) {
        HttpResponseMessageTimeoutInfo other = (HttpResponseMessageTimeoutInfo) o;
        int result = 0;
        if (trigger > other.trigger) {
            result = 1;
        } else if (trigger < other.trigger) {
            result = -1;
        } else {
            result = 0;
        }
        return result;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
}
