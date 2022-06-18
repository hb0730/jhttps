package com.hb0730.https;

import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.inter.AbstractSimpleHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import com.hb0730.https.support.httpclient.HttpClientImpl;
import com.hb0730.https.support.hutool.HutoolImpl;
import com.hb0730.https.support.okhttp3.OkHttp3Impl;
import com.hb0730.https.utils.ClassUtils;

import java.io.File;
import java.util.Map;

/**
 * 同步请求 工具类
 *
 * @author bing_huang
 * @since 4.0.0
 */
public class SimpleHttp implements com.hb0730.https.inter.SimpleHttp {
    /**
     * create http
     */
    public final static SimpleHttp HTTP = new SimpleHttp();
    private static AbstractSimpleHttp proxy;

    private void selectHttpProxy() {
        AbstractSimpleHttp defaultProxy = null;
        ClassLoader classLoader = SimpleHttp.class.getClassLoader();
        if (ClassUtils.isPresent("org.apache.http.impl.client.HttpClients", classLoader)) {
            defaultProxy = getHttpProxy(HttpClientImpl.class);
        }
        if (ClassUtils.isPresent("okhttp3.OkHttpClient", classLoader)) {
            defaultProxy = getHttpProxy(OkHttp3Impl.class);
        }
        if (ClassUtils.isPresent("cn.hutool.http.HttpRequest", classLoader)) {
            defaultProxy = getHttpProxy(HutoolImpl.class);
        }
        if (defaultProxy == null) {
            throw new HttpException("Has no HttpImpl defined in environment!");
        }
        proxy = defaultProxy;
    }

    private <T extends AbstractSimpleHttp> AbstractSimpleHttp getHttpProxy(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Throwable e) {
            return null;
        }
    }

    private synchronized void checkHttpNotNull(com.hb0730.https.inter.SimpleHttp proxy) {
        if (null == proxy) {
            selectHttpProxy();
        }
    }

    public SimpleHttp setHttp(AbstractSimpleHttp http) {
        proxy = http;
        return this;
    }


    public SimpleHttp setHttpConfig(HttpConfig config) {
        checkHttpNotNull(proxy);
        if (null == config) {
            config = HttpConfig.builder().timeout(Constants.DEFAULT_TIMEOUT).build();
        }
        proxy.setHttpConfig(config);
        return this;
    }

    /**
     * 设置请求头
     *
     * @param header {@link HttpHeader}
     * @return {@link SimpleHttp}
     * @since 2.0.3
     */
    public SimpleHttp setHeader(HttpHeader header) {
        checkHttpNotNull(proxy);
        proxy.setHeader(header);
        return this;
    }

    /**
     * GET 请求
     *
     * @param url URL
     * @return 结果
     */
    @Override
    public SimpleHttpResponse get(String url) {
        checkHttpNotNull(proxy);
        return proxy.get(url);
    }

    /**
     * GET 请求
     *
     * @param url    URL
     * @param params 参数
     * @return 结果
     */
    @Override
    public SimpleHttpResponse get(String url, Map<String, String> params) {
        checkHttpNotNull(proxy);
        return proxy.get(url, params);
    }

    /**
     * POST 请求
     *
     * @param url URL
     * @return 结果
     */
    @Override
    public SimpleHttpResponse postFormStr(String url) {
        checkHttpNotNull(proxy);
        return proxy.postFormStr(url);
    }

    /**
     * POST 请求
     *
     * @param url  URL
     * @param data JSON 参数
     * @return 结果
     */
    @Override
    public SimpleHttpResponse postFormStr(String url, String data) {
        checkHttpNotNull(proxy);
        return proxy.postFormStr(url, data);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url, String dataJson, HttpHeader header) {
        checkHttpNotNull(proxy);
        return proxy.postFormStr(url, dataJson, header);
    }

    /**
     * POST 请求
     *
     * @param url    URL
     * @param params form 参数
     * @return 结果
     */
    @Override
    public SimpleHttpResponse postFormStr(String url, Map<String, String> params) {
        checkHttpNotNull(proxy);
        return proxy.postFormStr(url, params);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url, Map<String, String> formData, HttpHeader header) {
        checkHttpNotNull(proxy);
        return proxy.postFormStr(url, formData, header);
    }

    @Override
    public SimpleHttpResponse postFile(String url, String name, String filename, byte[] fileBytes) {
        checkHttpNotNull(proxy);
        return proxy.postFile(url, name, filename, fileBytes);
    }

    @Override
    public SimpleHttpResponse postFile(String url, String name, File file) {
        checkHttpNotNull(proxy);
        return proxy.postFile(url, name, file);
    }

    @Override
    public SimpleHttpResponse postFormFile(String url, Map<String, Object> formData) {
        checkHttpNotNull(proxy);
        return proxy.postFormFile(url, formData);
    }

    @Override
    public SimpleHttpResponse postFormFile(String url, Map<String, Object> formData, HttpHeader header) {
        checkHttpNotNull(proxy);
        return proxy.postFormFile(url, formData, header);
    }
}
