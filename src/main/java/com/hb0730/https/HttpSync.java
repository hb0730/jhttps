package com.hb0730.https;

import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.inter.AbstractSyncHttp;
import com.hb0730.https.inter.SyncHttp;
import com.hb0730.https.support.httpclient.HttpClientSyncImpl;
import com.hb0730.https.support.hutool.HutoolSyncImpl;
import com.hb0730.https.support.okhttp3.OkHttp3SyncImpl;
import com.hb0730.https.utils.ClassUtils;

import java.io.InputStream;
import java.util.Map;

/**
 * 同步请求 工具类
 *
 * @author bing_huang
 * @since 1.0.0
 */
public class HttpSync implements SyncHttp {
    private static AbstractSyncHttp proxy;

    private void selectHttpProxy() {
        AbstractSyncHttp defaultProxy = null;
        ClassLoader classLoader = HttpSync.class.getClassLoader();
        if (ClassUtils.isPresent("org.apache.http.impl.client.HttpClients", classLoader)) {
            defaultProxy = getHttpProxy(HttpClientSyncImpl.class);
        }
        if (ClassUtils.isPresent("okhttp3.OkHttpClient", classLoader)) {
            defaultProxy = getHttpProxy(OkHttp3SyncImpl.class);
        }
        if (ClassUtils.isPresent("cn.hutool.http.HttpRequest", classLoader)) {
            defaultProxy = getHttpProxy(HutoolSyncImpl.class);
        }
        if (defaultProxy == null) {
            throw new HttpException("Has no HttpImpl defined in environment!");
        }
        proxy = defaultProxy;
    }

    private <T extends AbstractSyncHttp> AbstractSyncHttp getHttpProxy(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Throwable e) {
            return null;
        }
    }

    private synchronized void checkHttpNotNull(SyncHttp proxy) {
        if (null == proxy) {
            selectHttpProxy();
        }
    }

    public HttpSync setHttp(AbstractSyncHttp http) {
        proxy = http;
        return this;
    }


    public HttpSync setHttpConfig(HttpConfig config) {
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
     * @return {@link HttpSync}
     * @since 2.0.3
     */
    public HttpSync setHeader(HttpHeader header) {
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
    public String get(String url) {
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
    public String get(String url, Map<String, String> params) {
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
    public String post(String url) {
        checkHttpNotNull(proxy);
        return proxy.post(url);
    }

    /**
     * POST 请求
     *
     * @param url  URL
     * @param data JSON 参数
     * @return 结果
     */
    @Override
    public String post(String url, String data) {
        checkHttpNotNull(proxy);
        return proxy.post(url, data);
    }

    @Override
    public String post(String url, String dataJson, HttpHeader header) {
        checkHttpNotNull(proxy);
        return proxy.post(url, dataJson, header);
    }

    @Override
    public InputStream postStream(String url, String dataJson) {
        checkHttpNotNull(proxy);
        return proxy.postStream(url, dataJson);
    }

    /**
     * POST 请求
     *
     * @param url    URL
     * @param params form 参数
     * @return 结果
     */
    @Override
    public String post(String url, Map<String, String> params) {
        checkHttpNotNull(proxy);
        return proxy.post(url, params);
    }

    @Override
    public String post(String url, Map<String, String> formData, HttpHeader header) {
        checkHttpNotNull(proxy);
        return proxy.post(url, formData, header);
    }
}
