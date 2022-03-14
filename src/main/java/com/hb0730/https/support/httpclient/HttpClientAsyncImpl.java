package com.hb0730.https.support.httpclient;

import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.inter.AbstractAsyncHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import com.hb0730.https.support.callback.HttpCallback;
import com.hb0730.https.utils.CollectionUtils;
import com.hb0730.https.utils.MapUtils;
import com.hb0730.https.utils.StringUtils;
import lombok.Getter;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.support.BasicResponseConsumer;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.net.WWWFormCodec;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * HTTPClient async,需要自行关闭{@link #httpClient}
 *
 * @author bing_huang
 * @since 1.0.1
 */
public class HttpClientAsyncImpl extends AbstractAsyncHttp {
    @Getter
    private final CloseableHttpAsyncClient httpClient;

    public HttpClientAsyncImpl() {
        this(HttpAsyncClients.createDefault(), new HttpConfig());
    }

    public HttpClientAsyncImpl(HttpConfig config) {
        this(HttpAsyncClients.createDefault(), config);
    }

    public HttpClientAsyncImpl(CloseableHttpAsyncClient httpClient, HttpConfig config) {
        super(config);
        this.httpClient = httpClient;
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
        URI uri;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (!CollectionUtils.isEmpty(params)) {
                List<NameValuePair> query = new ArrayList<>(params.size());
                params.forEach((k, v) -> query.add(new BasicNameValuePair(k, v)));
                if (this.httpConfig.isEncode()) {
                    String queryParams = WWWFormCodec.format(query, getCharSet());
                    builder.setCustomQuery(queryParams);
                } else {
                    builder.addParameters(query);
                }
            }
            builder.setCharset(getCharSet());
            uri = builder.build();
        } catch (URISyntaxException e) {
            throw new HttpException(e);
        }
        SimpleRequestBuilder builder = SimpleRequestBuilder.get(uri);
        builder.setRequestConfig(buildConfig());

        SimpleHttpRequest httpRequest = builder.build();
        addHeader(httpRequest);
        SimpleRequestProducer producer = SimpleRequestProducer.create(httpRequest);
        this.exec(producer, httpCallback);
    }

    @Override
    public void post(String url, final HttpCallback httpCallback) {
        post(url, Constants.EMPTY, httpCallback);
    }

    @Override
    public void post(String url, String dataJson, HttpCallback httpCallback) {
        if (StringUtils.isBlank(url)) {
            throw new HttpException("url missing");
        }
        SimpleRequestBuilder builder = SimpleRequestBuilder.post(url);
        builder.setBody(dataJson, getContentType());
        builder.setRequestConfig(buildConfig());
        builder.setCharset(httpConfig.getCharset());
        SimpleHttpRequest httpRequest = builder.build();
        addHeader(httpRequest);
        SimpleRequestProducer producer = SimpleRequestProducer.create(httpRequest);
        this.exec(producer, httpCallback);
    }

    @Override
    public void post(String url, Map<String, String> formdata, HttpCallback httpCallback) {
        if (StringUtils.isBlank(url)) {
            throw new HttpException("url missing");
        }
        SimpleRequestBuilder builder = SimpleRequestBuilder.post(url);
        builder.setCharset(this.httpConfig.getCharset());
        if (!CollectionUtils.isEmpty(formdata)) {
            formdata.forEach(builder::addParameter);
        }
        SimpleHttpRequest httpRequest = builder.build();
        SimpleRequestProducer producer = SimpleRequestProducer.create(httpRequest);
        this.exec(producer, httpCallback);
    }

    private void exec(AsyncRequestProducer producer, HttpCallback httpCallback) {
        this.httpClient.start();
        this.httpClient.execute(producer, new BasicResponseConsumer<byte[]>(new BasicAsyncEntityConsumer()), new FutureCallback<Message<HttpResponse, byte[]>>() {
            @Override
            public void completed(Message<HttpResponse, byte[]> result) {
                HttpResponse response = result.getHead();
                boolean success = (response.getCode() >= HttpStatus.SC_SUCCESS && response.getCode() < HttpStatus.SC_REDIRECTION);
                Map<String, List<String>> headers = Arrays.stream(response.getHeaders())
                    .collect(Collectors.toMap(Header::getName,
                        (value) -> {
                            List<String> valueList = new ArrayList<>();
                            valueList.add(value.getValue());
                            return valueList;
                        }
                        , (v1Value, v2Value) -> v2Value));
                SimpleHttpResponse httpResponse = SimpleHttpResponse
                    .builder()
                    .success(success)
                    .headers(headers)
                    .build().body(result.getBody());
                try {
                    httpCallback.response(httpResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception ex) {
                if (null == httpCallback) {
                    return;
                }
                httpCallback.failure(ex);
            }

            @Override
            public void cancelled() {

            }
        });
    }

    private RequestConfig buildConfig() {
        RequestConfig.Builder builder = RequestConfig.custom()
            .setConnectionRequestTimeout(httpConfig.getTimeout(), TimeUnit.MILLISECONDS)
            .setConnectTimeout(httpConfig.getTimeout(), TimeUnit.MILLISECONDS);
        if (null != httpConfig.getProxy()) {
            Proxy proxy = httpConfig.getProxy();
            InetSocketAddress address = (InetSocketAddress) proxy.address();
            HttpHost httpHost = new HttpHost(proxy.type().name().toLowerCase(), address.getHostName(), address.getPort());
            builder.setProxy(httpHost);
        }
        return builder.build();
    }

    private void addHeader(HttpRequest request) {
        if (null != getHeader()) {
            Map<String, String> headers = getHeader().getHeaders();
            MapUtils.forEach(headers, request::addHeader);
        }
    }

    private Charset getCharSet() {
        return this.httpConfig.getCharset() == null ? StandardCharsets.UTF_8 : this.httpConfig.getCharset();
    }

    private ContentType getContentType() {
        String contentType = getHttpConfig().getContentType();
        if (StringUtils.isBlank(contentType)) {
            return ContentType.TEXT_PLAIN;
        } else {
            return ContentType.parse(contentType);
        }
    }
}
