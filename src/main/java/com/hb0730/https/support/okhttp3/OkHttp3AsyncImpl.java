package com.hb0730.https.support.okhttp3;

import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.inter.AbstractAsyncHttp;
import com.hb0730.https.support.callback.HttpCallback;
import com.hb0730.https.utils.StringUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Map;

/**
 * okhttp3 async impl
 *
 * @author bing_huang
 * @since 1.0.1
 */
public class OkHttp3AsyncImpl extends AbstractAsyncHttp implements IOkhttp3 {
    private final okhttp3.OkHttpClient.Builder clientBuilder;

    public OkHttp3AsyncImpl() {
        this(new HttpConfig());
    }

    public OkHttp3AsyncImpl(HttpConfig config) {
        this(new OkHttpClient().newBuilder(), config);
    }

    public OkHttp3AsyncImpl(okhttp3.OkHttpClient.Builder clientBuilder, HttpConfig config) {
        super(config);
        this.clientBuilder = clientBuilder;
    }

    @Override
    public void get(String url, HttpCallback httpCallback) {
        get(url, null, httpCallback);
    }

    @Override
    public void get(String url, Map<String, String> params, HttpCallback httpCallback) {
        if (StringUtils.isBlank(url)) {
            throw new HttpException("url missing");
        }
        Request.Builder builder = getRequestBuilder(url, params, this.httpConfig.isEncode(), this.header == null ? null : this.header.getHeaders());
        exec(builder, httpCallback);
    }

    @Override
    public void post(String url, HttpCallback httpCallback) {
        this.post(url, "", httpCallback);
    }

    @Override
    public void post(String url, String dataJson, HttpCallback httpCallback) {
        if (StringUtils.isBlank(url)) {
            throw new HttpException("url missing");
        }
        Request.Builder requestBuilder = postJsonRequestBuild(url, dataJson, StringUtils.isBlank(this.httpConfig.getContentType()) ? JSON :
            MediaType.parse(this.httpConfig.getContentType()), this.header == null ? null : this.header.getHeaders());
        exec(requestBuilder, httpCallback);
    }


    @Override
    public void post(String url, Map<String, String> formdata, HttpCallback httpCallback) {
        if (StringUtils.isBlank(url)) {
            throw new HttpException("url missing");
        }
        Request.Builder requestBuilder = postFormDataRequestBuild(url, formdata, this.httpConfig.isEncode(),
            StringUtils.isBlank(this.httpConfig.getContentType()) ?
                FORM_DATA : MediaType.parse(this.httpConfig.getContentType()),
            (null == this.header) ? null : this.header.getHeaders());
        exec(requestBuilder, httpCallback);
    }

    private void exec(Request.Builder requestBuilder, HttpCallback httpCallback) {
        Request request = requestBuilder.build();
        OkHttpClient httpClient = buildClient(this.clientBuilder, this.httpConfig);
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == httpCallback) {
                    return;
                }
                httpCallback.failure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == httpCallback) {
                    return;
                }
                try (ResponseBody responseBody = response.body()) {
                    if (response.isSuccessful()) {
                        httpCallback.success(responseBody.string());
                    }
                }
            }
        });
    }
}
