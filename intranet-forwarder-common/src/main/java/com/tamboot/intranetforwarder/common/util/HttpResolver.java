package com.tamboot.intranetforwarder.common.util;

import com.tamboot.common.tools.mapper.JsonMapper;
import com.tamboot.common.tools.text.EncodeUtil;
import com.tamboot.common.tools.text.EscapeUtil;
import com.tamboot.common.tools.text.TextUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class HttpResolver {
    public static String encodeBody(InputStream body) {
        if (body == null) {
            return TextUtil.EMPTY_STRING;
        }

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = body.read(buffer))) {
                output.write(buffer, 0, n);
            }
            byte[] bodyBytes =output.toByteArray();
            return EncodeUtil.encodeBase64(bodyBytes);
        } catch (IOException e) {
            return TextUtil.EMPTY_STRING;
        }
    }

    public static String encodeFormUrlencodedBody(Map<String, String> form) {
        if (form == null) {
            return TextUtil.EMPTY_STRING;
        }

        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> entry : form.entrySet()) {
            String name = entry.getKey() == null ? "" : entry.getKey();
            String value = entry.getValue() == null ? "" : entry.getValue();
            body.append(EscapeUtil.urlEncode(name));
            body.append("=");
            body.append(EscapeUtil.urlEncode(value));
            body.append("&");
        }

        return EncodeUtil.encodeBase64(body.toString().getBytes());
    }

    public static byte[] decodeBody(String body) {
        if (TextUtil.isBlank(body)) {
            return new byte[0];
        }

        return EncodeUtil.decodeBase64(body);
    }

    public static String encodeHeaders(Map<String, String> headers) {
        return JsonMapper.nonEmptyMapper().toJson(headers);
    }

    public static Map<String, String> decodeHeaders(String headersText) {
        return JsonMapper.nonEmptyMapper().fromJson(headersText, Map.class);
    }
}
