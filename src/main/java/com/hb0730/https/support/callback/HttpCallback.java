package com.hb0730.https.support.callback;

import com.hb0730.https.support.SimpleHttpResponse;

import java.io.IOException;

/**
 * 异步请求回调
 *
 * @author bing_huang
 * @since 1.0.1
 */
public interface HttpCallback {

    /**
     * 响应成功 200 &gt;= http code &lt; 300
     *
     * @param result 响应结果
     * @throws IOException 异常
     * @see #response(SimpleHttpResponse)
     */
    @Deprecated
    default void success(String result) throws IOException {
    }

    /**
     * 响应成功 200 &gt;= http code &lt; 300
     *
     * @param response 响应结果
     * @throws IOException 异常
     */
    void response(SimpleHttpResponse response) throws IOException;

    /**
     * 请求参数
     *
     * @param e 异常
     */
    @Deprecated
    default void failure(Exception e) {
    }
}
