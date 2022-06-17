package com.hb0730.https.support.okhttp3;

import cn.hutool.core.io.IoUtil;
import com.hb0730.https.HttpHeader;
import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.inter.AbstractSyncHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import com.hb0730.https.utils.StringUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * okhttp3 sync impl
 *
 * @author bing_huang
 * @since 1.0.0
 */
public class OkHttp3SyncImpl extends AbstractSyncHttp implements IOkhttp3 {
    private final okhttp3.OkHttpClient.Builder clientBuilder;

    public OkHttp3SyncImpl() {
        this(new HttpConfig());
    }

    public OkHttp3SyncImpl(HttpConfig config) {
        this(new OkHttpClient().newBuilder(), config);
    }

    public OkHttp3SyncImpl(okhttp3.OkHttpClient.Builder clientBuilder, HttpConfig config) {
        super(config);
        this.clientBuilder = clientBuilder;

    }

    @Override
    public SimpleHttpResponse get(String url) {
        return get(url, null);
    }

    @Override
    public SimpleHttpResponse get(String url, Map<String, String> params) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("url missing");
        }
        Request.Builder builder = getRequestBuilder(url, params,
            this.httpConfig.isEncode(),
            this.header == null ? null : this.header.getHeaders());
        return exec(builder);
    }

    @Override
    public SimpleHttpResponse post(String url) {
        return this.post(url, "");
    }


    @Override
    public SimpleHttpResponse post(String url, String data) {
        return post(url, data, null);
    }

    @Override
    public SimpleHttpResponse post(String url, String dataJson, HttpHeader header) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("url missing");
        }
        Request.Builder requestBuilder = postJsonRequestBuild(url, dataJson,
            StringUtils.isBlank(this.httpConfig.getContentType()) ?
                JSON_UTF_8 : MediaType.parse(this.httpConfig.getContentType()),
            this.header == null ? null : this.header.getHeaders());
        if (null != header) {
            header.getHeaders().forEach(requestBuilder::addHeader);
        }
        return exec(requestBuilder);
    }

    @Override
    public SimpleHttpResponse post(String url, Map<String, String> formdata) {
        return post(url, formdata, null);
    }

    @Override
    public SimpleHttpResponse post(String url, Map<String, String> formData, HttpHeader header) {
        if (StringUtils.isBlank(url)) {
            throw new HttpException("url missing");
        }
        Request.Builder requestBuilder = postFormDataRequestBuild(url, formData, this.httpConfig.isEncode(),
            StringUtils.isBlank(this.httpConfig.getContentType()) ? FORM_DATA_UTF_8 :
                MediaType.parse(this.httpConfig.getContentType()),
            null == this.header ? null : this.header.getHeaders());
        if (null != header) {
            header.getHeaders().forEach(requestBuilder::addHeader);
        }
        return exec(requestBuilder);
    }

    private SimpleHttpResponse exec(Request.Builder requestBuilder) {
        if (null == requestBuilder) {
            return SimpleHttpResponse.builder().success(false).build();
        }
        Request request = requestBuilder.build();
        OkHttpClient httpClient = buildClient(clientBuilder, this.httpConfig);
        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            InputStream stream = null;
            if (null != body) {
                stream = body.byteStream();
            }
            return SimpleHttpResponse.builder()
                .success(response.isSuccessful())
                .headers(response.headers().toMultimap())
                .body(IoUtil.readBytes(stream, false)).build();
        } catch (IOException e) {
            throw new HttpException("http execute error:" + e.getMessage(), e);
        }
    }
}
