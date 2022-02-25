package com.hb0730.https.support.hutool;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import com.hb0730.https.HttpHeader;
import com.hb0730.https.config.HttpConfig;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @since 3.0.0
 */
public interface IHutoolHttp {

    /**
     * create url builder
     *
     * @param url         url
     * @param queryParams query
     * @param charset     charset
     * @param isEnabled   if enabled x-www-form-urlencoded enabled
     * @return url builder
     */
    default UrlBuilder urlBuilder(String url, Map<String, String> queryParams, Charset charset, boolean isEnabled) {
        UrlQuery query = UrlQuery.of(queryParams, isEnabled);
        UrlBuilder builder = UrlBuilder.of(url, charset);
        UrlQuery urlQuery = builder.getQuery();
        query.addAll(urlQuery.getQueryMap());
        builder.setQuery(query);
        return builder;

    }

    /**
     * create hutool request
     *
     * @param builder url build
     * @param method  request method
     * @param config  http config
     * @param header  http request header
     * @return http request
     */
    default HttpRequest getHttpRequest(UrlBuilder builder, Method method, HttpConfig config, HttpHeader header) {
        HttpRequest request = new HttpRequest(builder);
        request.method(method);
        request.setProxy(config.getProxy());
        request.setConnectionTimeout(Math.toIntExact(config.getTimeout()));
        if (null != header) {
            request.addHeaders(header.getHeaders());
        }
        return request;
    }
}
