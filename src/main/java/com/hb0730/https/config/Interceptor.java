package com.hb0730.https.config;

import okhttp3.Response;

/**
 * http Interceptor
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2022/7/13
 */
public interface Interceptor {
    /**
     * request client,
     * <ul>
     *     <li>{@link  okhttp3.OkHttpClient}</li>
     *     <li>{@link  org.apache.http.impl.client.CloseableHttpClient}</li>
     *     <li>{@link  cn.hutool.http.HttpRequest}</li>
     * </ul>
     *
     * @param client request client
     * @param <C>    request client type
     */
    <C> void client(C client);

    /**
     * request
     * <ul>
     *     <li>{@link  okhttp3.Request}</li>
     *     <li>{@link  org.apache.http.client.methods.HttpUriRequest}</li>
     * </ul>
     *
     * @param request http request
     * @param <T>     http request type
     */
    <T> void request(T request);

    /**
     * http response
     * <ul>
     *     <li>{@link  Response}</li>
     *     <li>{@link  org.apache.http.client.methods.CloseableHttpResponse}</li>
     *     <li>{@link  cn.hutool.http.HttpResponse}</li>
     * </ul>
     *
     * @param response http response
     * @param <T>      http response type
     */
    <T> void response(T response);
}
