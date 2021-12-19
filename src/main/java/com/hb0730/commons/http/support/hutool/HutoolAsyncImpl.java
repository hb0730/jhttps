package com.hb0730.commons.http.support.hutool;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.hb0730.commons.http.config.HttpConfig;
import com.hb0730.commons.http.constants.Constants;
import com.hb0730.commons.http.exception.HttpException;
import com.hb0730.commons.http.inter.AbstractAsyncHttp;
import com.hb0730.commons.http.support.callback.HttpCallback;
import com.hb0730.commons.http.utils.MapUtils;
import com.hb0730.commons.http.utils.StringUtils;
import org.apache.hc.client5.http.ClientProtocolException;

import java.io.IOException;
import java.util.Map;

/**
 * hutool 异步
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2021/12/19
 * @since 3.0.0
 */
public class HutoolAsyncImpl extends AbstractAsyncHttp {
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
            throw new HttpException("request url must be not null");
        }
        String baseUrl = StringUtils.appendIfNotContain(url, "?", "&");
        baseUrl = baseUrl + MapUtils.parseMapToUrlString(params, this.httpConfig.isEncode());
        HttpRequest request = getRequest(baseUrl, Method.GET);
        response(httpCallback, request);

    }

    @Override
    public void post(String url, HttpCallback httpCallback) {
        post(url, (String) null, httpCallback);
    }

    @Override
    public void post(String url, String dataJson, HttpCallback httpCallback) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("request url must be not null");
        }
        HttpRequest request = getRequest(url, Method.POST);
        request.body(dataJson);
        response(httpCallback, request);
    }

    @Override
    public void post(String url, Map<String, String> formdata, HttpCallback httpCallback) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("request url must be not null");
        }
        HttpRequest request = getRequest(url, Method.POST);
        request.formStr(formdata);
        response(httpCallback, request);
    }

    private void response(HttpCallback httpCallback, HttpRequest request) {
        try (HttpResponse response = request.executeAsync()) {
            if (response.isOk()) {
                String body = IoUtil.read(response.bodyStream(), Constants.DEFAULT_ENCODING);
                httpCallback.success(body);
            } else {
                httpCallback.failure(new ClientProtocolException("Unexpected response status: " + response.getStatus()));
            }
        } catch (IOException e) {
            httpCallback.failure(e);
        }
    }

    private HttpRequest getRequest(String url, Method method) {
        HttpRequest request = new HttpRequest(url);
        request.setMethod(method);
        HttpConfig config = getHttpConfig();
        request.setProxy(config.getProxy());
        request.setConnectionTimeout(Math.toIntExact(config.getTimeout()));
        if (null != getHeader()) {
            request.addHeaders(getHeader().getHeaders());
        }
        return request;

    }
}
