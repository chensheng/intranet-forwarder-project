package com.tamboot.intranetforwarder.server.core;

import com.tamboot.common.tools.base.ExceptionUtil;
import com.tamboot.intranetforwarder.common.message.HttpResponseMessage;
import com.tamboot.intranetforwarder.server.config.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Semaphore;

public class HttpResponseMessageStore {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseMessageStore.class);

    private ConcurrentHashMap<String, HttpResponseMessage> msgStore = new ConcurrentHashMap<String, HttpResponseMessage>();

    private DelayQueue<HttpResponseMessageTimeoutInfo> timeoutQueue = new DelayQueue<HttpResponseMessageTimeoutInfo>();

    private TimeoutLooper timeoutLooper = new TimeoutLooper();

    private Semaphore startLooperSemaphore = new Semaphore(1);

    private ServerProperties serverProperties;

    public HttpResponseMessageStore(ServerProperties serverProperties) {
        if (serverProperties == null) {
            throw new NullPointerException("serverProperties may not be null");
        }
        this.serverProperties = serverProperties;
    }

    public void put(HttpResponseMessage message) {
        if (message == null) {
            return;
        }

        startLooperInNeed();
        HttpResponseMessageTimeoutInfo timeoutInfo = new HttpResponseMessageTimeoutInfo(message.body().getRequestId(), serverProperties.getHttpRequestTimeoutMillis());
        msgStore.put(message.body().getRequestId(), message);
        timeoutQueue.offer(timeoutInfo);
    }

    public HttpResponseMessage poll(String requestId) {
        if (requestId == null) {
            return null;
        }

        long deadline = System.currentTimeMillis() + serverProperties.getHttpRequestTimeoutMillis();
        while (System.currentTimeMillis() < deadline) {
            HttpResponseMessage responseMsg = msgStore.get(requestId);
            if (responseMsg != null) {
                return responseMsg;
            }
            try {
                Thread.sleep(serverProperties.getHttpResponsePollIntervalMillis());
            } catch (InterruptedException e) {
                logger.error(ExceptionUtil.stackTraceText(e));
                break;
            }
        }
        return null;
    }

    public void start() {
        startLooperInNeed();
    }

    public void stop() {
        timeoutLooper.cancel();
        msgStore.clear();
        timeoutQueue.clear();
    }

    private void startLooperInNeed() {
        if (timeoutLooper.isLooping()) {
            return;
        }

        if (!startLooperSemaphore.tryAcquire()) {
            return;
        }

        try {
            if (!timeoutLooper.isLooping()) {
                timeoutLooper.start();
            }
        } finally {
            startLooperSemaphore.release();
        }
    }

    private class TimeoutLooper extends Thread {
        private volatile boolean looping = false;

        TimeoutLooper() {
            setName("HttpResponseMessageTimeoutLooper-" + this.getId());
        }

        @Override
        public synchronized void start() {
            looping = true;
            super.start();
        }

        @Override
        public void run() {
            logger.info("HttpResponseMessageTimeoutLooper started...");
            try {
                while (looping) {
                    HttpResponseMessageTimeoutInfo info = timeoutQueue.take();
                    if (info == null) {
                        continue;
                    }

                    msgStore.remove(info.getRequestId());
                }
            } catch (InterruptedException e) {
                logger.error(e.toString());
            }

            logger.info("HttpResponseMessageTimeoutLooper shutdown...");
        }

        public boolean isLooping() {
            return looping;
        }

        public void cancel() {
            looping = false;
            this.interrupt();
        }
    }
}
