package com.hb0730.commons.http.support.httpclient;

import com.hb0730.commons.http.HttpHeader;
import com.hb0730.commons.http.config.HttpConfig;
import com.hb0730.commons.http.constants.Constants;
import com.hb0730.commons.http.exception.CommonHttpException;
import com.hb0730.commons.http.inter.AbstractSyncHttp;
import com.hb0730.commons.lang.StringUtils;
import com.hb0730.commons.lang.collection.CollectionUtils;
import com.hb0730.commons.lang.collection.MapUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http sync client 实现
 *
 * @author bing_huang
 * @date 2020/07/30 15:26
 * @since V1.0
 */
public class HttpClientSyncImpl extends AbstractSyncHttp {
    private final CloseableHttpClient httpClient;

    public HttpClientSyncImpl() {
        this(HttpClients.createDefault(), new HttpConfig());
    }

    public HttpClientSyncImpl(CloseableHttpClient httpClient) {
        this(httpClient, new HttpConfig());
    }

    public HttpClientSyncImpl(CloseableHttpClient httpClient, HttpConfig config) {
        super(config);
        this.httpClient = httpClient;
    }

    @Override
    public String get(String url) {
        return get(url, null, null);
    }

    @Override
    public String get(String url, Map<String, String> params) {
        return get(url, new HttpHeader(), params);
    }

    @Override
    public String get(String url, HttpHeader header, Map<String, String> params) {
        if (StringUtils.isEmpty(url)) {
            return Constants.EMPTY;
        }
        String baseUrl = StringUtils.appendIfNotContain(url, "?", "&");
        url = baseUrl + MapUtils.parseMapToUrlString(params, httpConfig.isEncode());
        HttpGet httpGet = new HttpGet(url);
        if (!CollectionUtils.isEmpty(params) || null != header) {
            MapUtils.forEach(header.getHeaders(), httpGet::addHeader);
        }
        return this.exec(httpGet);
    }

    @Override
    public String post(String url) {
        return post(url, Constants.EMPTY);
    }

    @Override
    public String post(String url, String dataJson) {
        return post(url, dataJson, null);
    }

    @Override
    public String post(String url, String dataJson, HttpHeader header) {
        if (StringUtils.isEmpty(url)) {
            return Constants.EMPTY;
        }
        HttpPost httpPost = new HttpPost(url);
        if (!StringUtils.isEmpty(dataJson)) {
            StringEntity entity = new StringEntity(dataJson, Constants.DEFAULT_ENCODING);
            entity.setContentEncoding(Constants.DEFAULT_ENCODING.displayName());
            entity.setContentType(Constants.CONTENT_TYPE_JSON);
            httpPost.setEntity(entity);
        }
        if (null != header) {
            MapUtils.forEach(header.getHeaders(), httpPost::addHeader);
        }
        return this.exec(httpPost);
    }

    @Override
    public String post(String url, Map<String, String> formdata) {
        return post(url, null, formdata);
    }

    @Override
    public String post(String url, HttpHeader header, Map<String, String> formdata) {
        if (StringUtils.isEmpty(url)) {
            return Constants.EMPTY;
        }
        HttpPost httpPost = new HttpPost(url);
        if (!CollectionUtils.isEmpty(formdata)) {
            List<NameValuePair> form = new ArrayList<>();
            MapUtils.forEach(formdata, (k, v) ->
                    form.add(new BasicNameValuePair(k, v)));
            httpPost.setEntity(new UrlEncodedFormEntity(form, Constants.DEFAULT_ENCODING));
        }
        if (null != header) {
            MapUtils.forEach(header.getHeaders(), httpPost::addHeader);
        }
        return this.exec(httpPost);
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

    private void addHeader(HttpRequestBase request) {
        if (null == request) {
            return;
        }
        String ua = Constants.USER_AGENT;
        Header[] headers = request.getHeaders(ua);
        if (null == headers || headers.length == 0) {
            request.setHeader(ua, Constants.USER_AGENT_DATA);
        }
    }

    private String exec(HttpRequestBase request) {
        this.addHeader(request);
        RequestConfig.Builder builder = RequestConfig.custom()
                .setConnectionRequestTimeout(httpConfig.getTimeout())
                .setConnectTimeout(httpConfig.getTimeout())
                .setSocketTimeout(httpConfig.getTimeout());
        if (null != httpConfig.getProxy()) {
            Proxy proxy = httpConfig.getProxy();
            InetSocketAddress address = (InetSocketAddress) proxy.address();
            HttpHost host = new HttpHost(address.getHostName(), address.getPort(), proxy.type().name().toLowerCase());
            builder.setProxy(host);
        }
        String result = Constants.EMPTY;
        request.setConfig(builder.build());
        try (CloseableHttpResponse response = this.httpClient.execute(request)) {
            if (!isSuccess(response)) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                result = EntityUtils.toString(entity, Constants.DEFAULT_ENCODING);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonHttpException("request result error:" + e.getMessage());
        }
        return result;
    }
}
