package com.hb0730.https.support.okhttp3;

import com.hb0730.https.HttpHeader;
import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.inter.AbstractSyncHttp;
import com.hb0730.https.utils.StringUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

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
    public String get(String url) {
        return get(url, null);
    }

    @Override
    public String get(String url, Map<String, String> params) {
        if (StringUtils.isEmpty(url)) {
            return Constants.EMPTY;
        }
        Request.Builder builder = getRequestBuilder(url, params,
            this.httpConfig.isEncode(),
            this.header == null ? null : this.header.getHeaders());
        return execStr(builder);
    }

    @Override
    public String post(String url) {
        return this.post(url, "");
    }


    @Override
    public String post(String url, String data) {
        if (StringUtils.isEmpty(url)) {
            return Constants.EMPTY;
        }
        Request.Builder requestBuilder = postJsonRequestBuild(url, data,
            StringUtils.isBlank(this.httpConfig.getContentType()) ?
                JSON_UTF_8 : MediaType.parse(this.httpConfig.getContentType()),
            this.header == null ? null : this.header.getHeaders());
        return execStr(requestBuilder);
    }

    @Override
    public String post(String url, String dataJson, HttpHeader header) {
        if (StringUtils.isEmpty(url)) {
            return Constants.EMPTY;
        }
        Request.Builder requestBuilder = postJsonRequestBuild(url, dataJson,
            StringUtils.isBlank(this.httpConfig.getContentType()) ?
                JSON_UTF_8 : MediaType.parse(this.httpConfig.getContentType()),
            this.header == null ? null : this.header.getHeaders());
        if (null!=header){
            header.getHeaders().forEach(requestBuilder::addHeader);
        }
        return execStr(requestBuilder);
    }

    @Override
    public InputStream postStream(String url, String dataJson) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        Request.Builder requestBuilder = postJsonRequestBuild(url, dataJson,
            StringUtils.isBlank(this.httpConfig.getContentType()) ?
                JSON_UTF_8 : MediaType.parse(this.httpConfig.getContentType()),
            this.header == null ? null : this.header.getHeaders());
        return execStream(requestBuilder);
    }

    @Override
    public String post(String url, Map<String, String> formdata) {
        if (StringUtils.isBlank(url)) {
            return Constants.EMPTY;
        }
        Request.Builder requestBuilder = postFormDataRequestBuild(url, formdata, this.httpConfig.isEncode(),
            StringUtils.isBlank(this.httpConfig.getContentType()) ? FORM_DATA_UTF_8 :
                MediaType.parse(this.httpConfig.getContentType()),
            null == this.header ? null : this.header.getHeaders());
        return execStr(requestBuilder);
    }

    public String execStr(Request.Builder requestBuilder) {
        String result = Constants.EMPTY;
        if (null == requestBuilder) {
            return result;
        }

        Request request = requestBuilder.build();
        OkHttpClient httpClient = buildClient(clientBuilder, this.httpConfig);
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                result = Objects.requireNonNull(response.body()).string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpException("http execute error:" + e.getMessage(), e);
        }
        return result;

    }

    public InputStream execStream(Request.Builder requestBuilder) {
        if (null == requestBuilder) {
            return null;
        }

        Request request = requestBuilder.build();
        OkHttpClient httpClient = buildClient(clientBuilder, this.httpConfig);
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return Objects.requireNonNull(response.body()).byteStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpException("http execute error:" + e.getMessage(), e);
        }
        return null;

    }
}
