package com.hb0730.https.support.okhttp3;

import cn.hutool.core.io.FileUtil;
import com.hb0730.https.HttpHeader;
import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.support.AbstractSimpleHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import com.hb0730.https.utils.StringUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * okhttp3 sync impl
 *
 * @author bing_huang
 * @since 1.0.0
 */
public class OkHttp3Impl extends AbstractSimpleHttp implements IOkhttp3 {
    private final okhttp3.OkHttpClient.Builder clientBuilder;

    public OkHttp3Impl() {
        this(HttpConfig.builder().build());
    }

    public OkHttp3Impl(HttpConfig config) {
        this(new OkHttpClient().newBuilder(), config);
    }

    public OkHttp3Impl(okhttp3.OkHttpClient.Builder clientBuilder) {
        this(clientBuilder, HttpConfig.builder().build());
    }

    public OkHttp3Impl(okhttp3.OkHttpClient.Builder clientBuilder, HttpConfig config) {
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
    public SimpleHttpResponse postFormStr(String url, Map<String, String> formdata) {
        return postFormStr(url, formdata, null);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url, Map<String, String> formData, HttpHeader header) {
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

    @Override
    public SimpleHttpResponse postFile(String url, String name, String filename, byte[] fileBytes) {
        File file = FileUtil.writeBytes(fileBytes, System.getProperty("java.io.tmpdir") + File.separator + filename);
        return this.postFile(url, name, file);
    }

    @Override
    public SimpleHttpResponse postFile(String url, String name, File file) {
        final Map<String, Object> body = new HashMap<String, Object>(1);
        body.put(name, file);
        return this.postFormFile(url, body);
    }

    @Override
    public SimpleHttpResponse postFormFile(String url, Map<String, Object> formData) {
        return this.postFormFile(url, formData, null);
    }

    @Override
    public SimpleHttpResponse postFormFile(String url, Map<String, Object> formData, HttpHeader header) {
        if (StringUtils.isBlank(url)) {
            throw new HttpException("url missing");
        }
        Request.Builder requestBuilder = postFormFileRequestBuild(url, formData,
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
            return SimpleHttpResponse.builder()
                    .success(response.isSuccessful())
                    .headers(response.headers().toMultimap())
                    .body(null != body ? body.bytes() : null).build();
        } catch (IOException e) {
            throw new HttpException("http execute error:" + e.getMessage(), e);
        }
    }
}
