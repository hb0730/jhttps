package com.hb0730.https.support.hutool;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.inter.AbstractAsyncHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import com.hb0730.https.support.callback.HttpCallback;
import com.hb0730.https.utils.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * hutool 异步
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @since 3.0.0
 */
public class HutoolAsyncImpl extends AbstractAsyncHttp implements IHutoolHttp {
    public HutoolAsyncImpl() {
        this(HttpConfig.builder().build());
    }

    public HutoolAsyncImpl(HttpConfig httpConfig) {
        super(httpConfig);
    }

    @Override
    public void get(String url, HttpCallback httpCallback) {
        get(url, null, httpCallback);
    }

    @Override
    public void get(String url, Map<String, String> params, HttpCallback httpCallback) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("url missing");
        }
        UrlBuilder builder = urlBuilder(url, params, httpConfig.getCharset(), httpConfig.isEncode());
        HttpRequest request = getHttpRequest(builder, Method.GET, httpConfig, this.header);
        response(httpCallback, request);

    }

    @Override
    public void post(String url, HttpCallback httpCallback) {
        post(url, (String) null, httpCallback);
    }

    @Override
    public void post(String url, String dataJson, HttpCallback httpCallback) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("url missing");
        }
        UrlBuilder builder = urlBuilder(url, null, httpConfig.getCharset(), httpConfig.isEncode());
        HttpRequest request = getHttpRequest(builder, Method.POST, this.httpConfig, this.header);
        request.body(dataJson, getContentType(Constants.CONTENT_TYPE_JSON_UTF_8));
        response(httpCallback, request);
    }

    @Override
    public void post(String url, Map<String, String> formdata, HttpCallback httpCallback) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("url missing");
        }
        UrlBuilder builder = urlBuilder(url, null, httpConfig.getCharset(), httpConfig.isEncode());
        HttpRequest request = getHttpRequest(builder, Method.POST, this.httpConfig, this.header);
        request.formStr(formdata);
        request.contentType(getContentType(Constants.CONTENT_TYPE_FORM_DATA_UTF_8));
        response(httpCallback, request);
    }

    private void response(HttpCallback httpCallback, HttpRequest request) {
        try (HttpResponse response = request.executeAsync()) {
            SimpleHttpResponse.SimpleHttpResponseBuilder body = SimpleHttpResponse.builder()
                .success(response.isOk())
                .headers(response.headers())
                .body(response.bodyBytes());
            httpCallback.response(body.build());
        } catch (IOException e) {
            httpCallback.failure(e);
        }
    }

    private String getContentType(String defaultContentType) {
        if (StringUtils.isBlank(this.httpConfig.getContentType())) {
            return defaultContentType;
        } else {
            return this.httpConfig.getContentType();
        }
    }
}
