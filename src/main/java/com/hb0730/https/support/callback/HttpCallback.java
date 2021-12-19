package com.hb0730.https.support.callback;

import com.hb0730.https.support.httpclient.HttpClientAsyncImpl;
import com.hb0730.https.support.okhttp3.OkHttp3AsyncImpl;
import okhttp3.Request;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;

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
     * @see OkHttp3AsyncImpl#exec(Request.Builder, HttpCallback)
     * @see HttpClientAsyncImpl#exec(HttpUriRequestBase, BasicRequestProducer, HttpCallback)
     */
    void success(String result) throws IOException;

    /**
     * 请求参数
     *
     * @param e 异常
     */
    void failure(Exception e);

}
