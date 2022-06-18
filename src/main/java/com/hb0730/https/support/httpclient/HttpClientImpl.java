package com.hb0730.https.support.httpclient;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hb0730.https.HttpHeader;
import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.inter.AbstractSimpleHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import com.hb0730.https.utils.CollectionUtils;
import com.hb0730.https.utils.MapUtils;
import com.hb0730.https.utils.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * http client 实现
 *
 * @author bing_huang
 * @since 4.0.0
 */
public class HttpClientImpl extends AbstractSimpleHttp {
    private final CloseableHttpClient httpClient;

    public HttpClientImpl() {
        this(HttpConfig.builder().build(), HttpClients.createDefault());
    }

    public HttpClientImpl(CloseableHttpClient httpClient) {
        super(HttpConfig.builder().build());
        this.httpClient = httpClient;
    }

    public HttpClientImpl(HttpConfig httpConfig, CloseableHttpClient httpClient) {
        super(httpConfig);
        this.httpClient = httpClient;
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
        URI uri;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (!CollectionUtils.isEmpty(params)) {
                List<NameValuePair> query = new ArrayList<>(params.size());
                params.forEach((k, v) -> query.add(new BasicNameValuePair(k, v)));
                if (this.httpConfig.isEncode()) {
                    String queryParams = URLEncodedUtils.format(query, getCharSet());
                    builder.setCustomQuery(queryParams);
                } else {
                    builder.addParameters(query);
                }
            }
            builder.setCharset(this.httpConfig.getCharset());
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new HttpException(e);
        }
        RequestBuilder builder = RequestBuilder.get(uri);
        builder.setCharset(getCharSet());
        builder.setConfig(buildConfig());
        HttpUriRequest request = builder.build();
        addHeader(request);
        return this.exec(request);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url) {
        return postFormStr(url, Constants.EMPTY);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url, String dataJson) {
        return postFormStr(url, dataJson, null);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url, String dataJson, HttpHeader header) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("url missing");
        }
        RequestBuilder builder = RequestBuilder.post(url);
        if (!StringUtils.isBlank(dataJson)) {
            StringEntity entity = new StringEntity(dataJson, getContentType());
            builder.setEntity(entity);
        }
        builder.setConfig(buildConfig());
        builder.setCharset(getCharSet());
        HttpUriRequest uriRequest = builder.build();
        addHeader(uriRequest);
        addHeader(uriRequest, header);
        return this.exec(uriRequest);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url, Map<String, String> formdata) {
        return postFormStr(url, formdata, null);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url, Map<String, String> formData, HttpHeader header) {
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("url missing");
        }
        RequestBuilder builder = RequestBuilder.post(url);
        if (!CollectionUtils.isEmpty(formData)) {
            List<NameValuePair> form = new ArrayList<>(formData.size());
            MapUtils.forEach(formData, (k, v) -> form.add(new BasicNameValuePair(v, k)));
            builder.setEntity(new UrlEncodedFormEntity(form, getCharSet()));
        }
        builder.setCharset(getCharSet());
        builder.setConfig(buildConfig());
        HttpUriRequest uriRequest = builder.build();
        addHeader(uriRequest);
        addHeader(uriRequest, header);
        return this.exec(uriRequest);
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
        if (StringUtils.isEmpty(url)) {
            throw new HttpException("url missing");
        }
        RequestBuilder builder = RequestBuilder.post(url);
        if (!CollectionUtils.isEmpty(formData)) {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            for (Map.Entry<String, Object> entry : formData.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (StrUtil.isBlank(name) || ObjectUtil.isNull(value)) {
                    continue;
                }
                if (value instanceof File) {
                    entityBuilder.addBinaryBody(name, (File) value);
                    continue;
                }
                // 普通值
                String strValue;
                if (value instanceof Iterable) {
                    // 列表对象
                    strValue = CollUtil.join((Iterable<?>) value, ",");
                } else if (ArrayUtil.isArray(value)) {
                    if (File.class == ArrayUtil.getComponentType(value)) {
                        File[] files = (File[]) value;
                        for (int i = 0; i < files.length; i++) {
                            entityBuilder.addPart(
                                FormBodyPartBuilder
                                    .create()
                                    .setBody(new FileBody(files[i]))
                                    .setName(name + i)
                                    .build());
                        }

                        continue;
                    }
                    // 数组对象
                    strValue = ArrayUtil.join((Object[]) value, ",");
                } else {
                    // 其他对象一律转换为字符串
                    strValue = Convert.toStr(value, null);
                }
                entityBuilder.addTextBody(name, strValue);
            }
            builder.setEntity(entityBuilder.build());
        }
        builder.setCharset(getCharSet());
        builder.setConfig(buildConfig());
        HttpUriRequest uriRequest = builder.build();
        addHeader(uriRequest);
        addHeader(uriRequest, header);
        return this.exec(uriRequest);
    }

    private boolean isSuccess(CloseableHttpResponse response) {
        if (response == null) {
            return false;
        }
        if (response.getStatusLine() == null) {
            return false;
        }
        return response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300;
    }

    /**
     * 设置请求头信息
     *
     * @param request 请求方式
     * @param header  请求头参数信息
     * @since 2.0.0
     */
    private void addHeader(HttpRequest request, HttpHeader header) {
        if (null == request || null == header) {
            return;
        }
        Map<String, String> headers = header.getHeaders();
        if (CollectionUtils.isEmpty(headers)) {
            return;
        }
        MapUtils.forEach(headers, request::addHeader);
    }


    private void addHeader(HttpRequest request) {
        addHeader(request, getHeader());
    }

    private SimpleHttpResponse exec(HttpUriRequest request) {
        try (CloseableHttpResponse response = this.httpClient.execute(request)) {
            boolean success = isSuccess(response);
            HttpEntity entity = response.getEntity();
            Map<String, List<String>> headers = Arrays.stream(response.getAllHeaders())
                .collect(Collectors.toMap(Header::getName,
                    (value) -> {
                        List<String> valueList = new ArrayList<>();
                        valueList.add(value.getValue());
                        return valueList;
                    }
                    , (v1Value, v2Value) -> v2Value));
            return SimpleHttpResponse.builder()
                .success(success)
                .body(IoUtil.readBytes(entity.getContent(), false)).headers(headers).build();
        } catch (IOException e) {
            throw new HttpException("request error:" + e.getMessage());
        }
    }

    private RequestConfig buildConfig() {
        int timeout;
        if (httpConfig.getTimeout() > Integer.MAX_VALUE) {
            timeout = Integer.MAX_VALUE;
        } else {
            timeout = Long.valueOf(httpConfig.getTimeout()).intValue();
        }
        RequestConfig.Builder builder = RequestConfig.custom().setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).setSocketTimeout(timeout);
        if (null != httpConfig.getProxy()) {
            Proxy proxy = httpConfig.getProxy();
            InetSocketAddress address = (InetSocketAddress) proxy.address();
            HttpHost host = new HttpHost(address.getHostName(), address.getPort(), proxy.type().name().toLowerCase());
            builder.setProxy(host);
        }
        return builder.build();
    }

    private Charset getCharSet() {
        return this.httpConfig.getCharset() == null ? StandardCharsets.UTF_8 : this.httpConfig.getCharset();
    }

    private ContentType getContentType() {
        String contentType = this.httpConfig.getContentType();
        if (StringUtils.isBlank(contentType)) {
            return ContentType.TEXT_PLAIN;
        } else {
            return ContentType.parse(contentType);
        }
    }
}
