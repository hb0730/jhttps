package com.hb0730.https.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 常量
 *
 * @author bing_huang
 * @since 1.0.0
 */
public interface Constants {
    /**
     * 超时时长，单位毫秒
     */
    long DEFAULT_TIMEOUT = 3000L;
    /**
     * 编码格式
     */
    Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
    /**
     * JSON
     */
    String CONTENT_TYPE_JSON_UTF_8 = "application/json; charset=utf-8";
    /**
     * form-data
     */
    String CONTENT_TYPE_FORM_DATA_UTF_8 = "application/x-www-form-urlencoded; charset=utf-8";

    /**
     * multipart/form-data
     */
    String MULTIPART = "multipart/form-data";

    /**
     * Content-Type
     */
    String CONTENT_TYPE = "Content-Type";

    /**
     * Content-Encoding
     */
    String CONTENT_ENCODING = "Content-Encoding";

    /**
     * User-Agent
     */
    String USER_AGENT = "User-Agent";

    /**
     * 模拟 User-Agent
     */
    String USER_AGENT_DATA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36 simple-http";

    /**
     * 空字符串
     */
    String EMPTY = "";
}
