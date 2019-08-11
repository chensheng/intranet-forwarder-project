package com.tamboot.intranetforwarder.client.listener;

import com.tamboot.common.tools.base.ExceptionUtil;
import com.tamboot.common.tools.text.EncodeUtil;
import com.tamboot.common.tools.text.TextUtil;
import com.tamboot.intranetforwarder.client.http.PoolingHttpClient;
import com.tamboot.intranetforwarder.common.message.HttpRequestMessage;
import com.tamboot.intranetforwarder.common.message.HttpResponseMessage;
import com.tamboot.intranetforwarder.common.message.body.HttpRequestMessageBody;
import com.tamboot.intranetforwarder.common.util.HttpResolver;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.chensheng.wsmessenger.client.MessengerClient;
import space.chensheng.wsmessenger.client.listener.ClientMessageListener;
import space.chensheng.wsmessenger.common.util.JsonMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HttpRequestMessageListener extends ClientMessageListener<HttpRequestMessage> {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestMessageListener.class);

    private static final String ACCEPT_HEADER_NAME = "Accept";

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    private PoolingHttpClient poolingHttpClient;

    @Override
    protected void onMessage(HttpRequestMessage httpRequestMessage, MessengerClient messengerClient) {
        if (logger.isDebugEnabled()) {
            logger.debug("receive http request message: " + JsonMapper.nonEmptyMapper().toJson(httpRequestMessage.body()));
        }

        HttpResponse httpResponse = null;
        try {
            HttpUriRequest httpUriRequest = toHttpUriRequest(httpRequestMessage.body());
            httpResponse = poolingHttpClient.get().execute(httpUriRequest);
            HttpResponseMessage respMsg = toHttpResponseMessage(httpResponse, httpRequestMessage);
            messengerClient.sendMessage(respMsg);
        } catch (URISyntaxException e) {
            logger.error(ExceptionUtil.stackTraceText(e));
        } catch (ClientProtocolException e) {
            logger.error(ExceptionUtil.stackTraceText(e));
        } catch (IOException e) {
            logger.error(ExceptionUtil.stackTraceText(e));
        } finally {
            if (httpResponse != null && httpResponse.getEntity() != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                } catch (IOException e) {
                    logger.error(ExceptionUtil.stackTraceText(e));
                }
            }
        }
    }

    private HttpResponseMessage toHttpResponseMessage(HttpResponse httpResponse, HttpRequestMessage httpRequestMessage) throws IOException {
        if (httpResponse == null) {
            return new HttpResponseMessage(httpRequestMessage.body().getRequestId(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null, null);
        }

        Map<String, String> headers = new HashMap<String, String>();
        Header[] allHeaders = httpResponse.getAllHeaders();
        if (allHeaders != null) {
            for (Header header : allHeaders) {
                headers.put(header.getName(), header.getValue());
            }
        }

        String body = HttpResolver.encodeBody(httpResponse.getEntity().getContent());
        return new HttpResponseMessage(httpRequestMessage.body().getRequestId(),
                HttpServletResponse.SC_OK,
                JsonMapper.nonEmptyMapper().toJson(headers),
                body);
    }

    private HttpUriRequest toHttpUriRequest(HttpRequestMessageBody request) throws URISyntaxException {
        RequestBuilder requestBuilder = RequestBuilder.create(request.getMethod());
        URI uri = setURI(requestBuilder, request);
        setQueryParams(requestBuilder, uri);
        setAuthorizationHeader(requestBuilder, uri);
        setHeaders(requestBuilder, request);
        setBody(requestBuilder, request);
        return requestBuilder.build();
    }

    private URI setURI(RequestBuilder requestBuilder, HttpRequestMessageBody request) throws URISyntaxException {
        String url = request.getUrl();
        if (TextUtil.isNotBlank(request.getQueryString())) {
            if (url.contains("?")) {
                url += request.getQueryString();
            } else {
                url += "?" + request.getQueryString();
            }
        }
        URI uri = new URIBuilder(url).build();
        requestBuilder.setUri(uri.getScheme() + "://" + uri.getAuthority() + uri.getRawPath());
        return uri;
    }

    private void setQueryParams(RequestBuilder requestBuilder, URI uri) {
        List<NameValuePair> queryParams = URLEncodedUtils.parse(uri, requestBuilder.getCharset());
        for (NameValuePair queryParam : queryParams) {
            requestBuilder.addParameter(queryParam);
        }
    }

    private void setAuthorizationHeader(RequestBuilder requestBuilder, URI uri) {
        String userInfo = uri.getUserInfo();
        if (TextUtil.isBlank(userInfo)) {
            return;
        }

        String authorization = "Basic " + EncodeUtil.encodeBase64(userInfo.getBytes());
        requestBuilder.setHeader("Authorization", authorization);
    }

    private void setHeaders(RequestBuilder requestBuilder, HttpRequestMessageBody request) {
        Map<String, String> headers = HttpResolver.decodeHeaders(request.getHeaders());
        boolean hasAcceptHeader = false;
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                String headerName = headerEntry.getKey();
                if (headerName.equalsIgnoreCase(ACCEPT_HEADER_NAME)) {
                    hasAcceptHeader = true;
                }

                if (headerName.equalsIgnoreCase(CONTENT_LENGTH)) {
                    continue;
                }

                requestBuilder.addHeader(headerName, headerEntry.getValue());
            }
        }
        if (!hasAcceptHeader) {
            requestBuilder.addHeader(ACCEPT_HEADER_NAME, "*/*");
        }
    }

    private void setBody(RequestBuilder requestBuilder,  HttpRequestMessageBody request) {
        byte[] bodyBytes = HttpResolver.decodeBody(request.getBody());
        requestBuilder.setEntity(new ByteArrayEntity(bodyBytes));
    }
}
