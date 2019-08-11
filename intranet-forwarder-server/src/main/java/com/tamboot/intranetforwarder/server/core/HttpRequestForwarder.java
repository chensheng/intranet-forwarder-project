package com.tamboot.intranetforwarder.server.core;

import com.tamboot.common.tools.mapper.JsonMapper;
import com.tamboot.common.tools.text.MD5Util;
import com.tamboot.common.tools.text.TextUtil;
import com.tamboot.intranetforwarder.common.contants.HttpContentType;
import com.tamboot.intranetforwarder.common.contants.HttpRequestHeader;
import com.tamboot.intranetforwarder.common.contants.HttpRequestMethod;
import com.tamboot.intranetforwarder.common.message.HttpRequestMessage;
import com.tamboot.intranetforwarder.common.message.HttpResponseMessage;
import com.tamboot.intranetforwarder.common.util.HttpResolver;
import com.tamboot.intranetforwarder.server.config.ServerProperties;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import space.chensheng.wsmessenger.server.WsMessengerServer;

import javax.activation.MimeType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class HttpRequestForwarder {
    private static final String FORWARDER_PREFIX = "/forwarder";

    private static final String SEPERATOR = "::";

    private AtomicLong requestSequence = new AtomicLong(0);

    @Autowired
    private WsMessengerServer messengerServer;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private HttpResponseMessageStore httpResponseMessageStore;

    public void forward(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String forwardUri = resolveForwardUri(request);
        ServerProperties.Forward forward = findForward(forwardUri);
        if (forward == null) {
            send404(response);
            return;
        }

        HttpRequestMessage message = createHttpRequestMessage(request, forward, forwardUri);
        messengerServer.sendMessageReliably(message, forward.getClientId());
        HttpResponseMessage respMsg = httpResponseMessageStore.poll(message.body().getRequestId());
        if (respMsg == null) {
            sendTimeout(response);
            return;
        }

        if (respMsg.body().getStatusCode() != HttpServletResponse.SC_OK) {
            sendError(response, respMsg.body().getStatusCode());
            return;
        }

        sendOk(response, respMsg);
    }

    private void sendOk(HttpServletResponse response, HttpResponseMessage respMsg) throws IOException {
        String headersJson = respMsg.body().getHeaders();
        Map<String, String> headers = JsonMapper.nonNullMapper().fromJson(headersJson, Map.class);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                response.setHeader(entry.getKey(), entry.getValue());
            }
        }

        String body = respMsg.body().getBody();
        byte[] bodyBytes = HttpResolver.decodeBody(body);
        response.getOutputStream().write(bodyBytes);
        response.getOutputStream().flush();
    }

    private void sendError(HttpServletResponse response, int statusCode) throws IOException {
        response.sendError(statusCode);
    }

    private void send404(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void sendTimeout(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_GATEWAY_TIMEOUT);
    }

    private HttpRequestMessage createHttpRequestMessage(HttpServletRequest request, ServerProperties.Forward forward, String forwardUri) throws IOException {
        String requestId = resolveRequestId(request);
        String url = resolveUrl(forward, forwardUri);
        String method = resolveMethod(request);
        String headers = resolveHeaders(request);
        String queryString = resolveQueryString(request);
        String body = resolveBody(method, request);
        return new HttpRequestMessage(requestId, url, method, headers, queryString, body);
    }

    private ServerProperties.Forward findForward(String forwardUri) {
        if (serverProperties.getForwards() == null || serverProperties.getForwards().length == 0) {
            return null;
        }

        for (ServerProperties.Forward forward : serverProperties.getForwards()) {
            if (TextUtil.isEmpty(forward.getClientId()) || TextUtil.isEmpty(forward.getTo())) {
                continue;
            }

            if (TextUtil.isEmpty(forward.getFrom()) || forwardUri.startsWith(forward.getFrom())) {
                return forward;
            }
        }

        return null;
    }

    private String resolveRequestId(HttpServletRequest request) {
        StringBuilder requestInfo = new StringBuilder();
        requestInfo.append(System.currentTimeMillis()).append(SEPERATOR);
        requestInfo.append(requestSequence.getAndIncrement()).append(SEPERATOR);
        requestInfo.append(ThreadLocalRandom.current().nextLong()).append(SEPERATOR);
        requestInfo.append(request.getRequestURI()).append(SEPERATOR);
        requestInfo.append(request.getMethod()).append(SEPERATOR);
        requestInfo.append(request.getQueryString()).append(SEPERATOR);
        requestInfo.append(request.getRequestedSessionId()).append(SEPERATOR);
        return MD5Util.md5With32(requestInfo.toString());
    }

    private String resolveForwardUri(HttpServletRequest request) {
        String pathPrefix = request.getContextPath() + FORWARDER_PREFIX;
        String requestUri = request.getRequestURI();
        int startIndex = pathPrefix.length();
        if (startIndex >= requestUri.length()) {
            return TextUtil.EMPTY_STRING;
        }

        return requestUri.substring(startIndex);
    }

    private String resolveUrl(ServerProperties.Forward forward, String forwardUri) {
        if (TextUtil.isEmpty(forward.getTo())) {
            return TextUtil.EMPTY_STRING;
        }

        if (TextUtil.isEmpty(forward.getFrom()) || TextUtil.isEmpty(forwardUri)) {
            return forward.getTo();
        }

        int startIndex = forward.getFrom().length();
        if (startIndex >= forwardUri.length()) {
            return forward.getTo();
        }

        return forward.getTo() + forwardUri.substring(startIndex);
    }

    private String resolveMethod(HttpServletRequest request) {
        return request.getMethod().toUpperCase();
    }

    private String resolveHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.put(headerName, headerValue);
            }
        }
        return HttpResolver.encodeHeaders(headers);
    }

    private String resolveQueryString(HttpServletRequest request) {
        return request.getQueryString();
    }

    private String resolveBody(String method, HttpServletRequest request) throws IOException {
        if (!HttpRequestMethod.POST.equals(method)) {
            return null;
        }

        String body = null;
        String contentType = request.getHeader(HttpRequestHeader.CONTENT_TYPE);
        if (contentType != null && contentType.contains(HttpContentType.X_WWW_FORM_URLENCODED)) {
            Map<String, String> form = new HashMap<String, String>();
            Enumeration<String> names = request.getParameterNames();
            if (names != null) {
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    form.put(name, request.getParameter(name));
                }
            }
            body = HttpResolver.encodeFormUrlencodedBody(form);
        } else {
            body = HttpResolver.encodeBody(request.getInputStream());
        }
        return body;
    }
}
