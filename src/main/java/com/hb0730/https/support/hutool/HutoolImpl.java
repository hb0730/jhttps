package com.hb0730.https.support.hutool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.hb0730.https.HttpHeader;
import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.inter.AbstractSimpleHttp;
import com.hb0730.https.inter.SimpleHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import com.hb0730.https.utils.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * hutool http
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @since 4.0.0
 */
public class HutoolImpl extends AbstractSimpleHttp implements IHutoolHttp {
    public HutoolImpl() {
        this(HttpConfig.builder().build());
    }

    public HutoolImpl(HttpConfig httpConfig) {
        super(httpConfig);
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
        UrlBuilder builder = urlBuilder(url, params, httpConfig.getCharset(), httpConfig.isEncode());
        HttpRequest request = getRequest(builder, Method.GET);
        return this.exec(request);
    }

    @Override
    public SimpleHttpResponse postFormStr(String url) {
        return this.postFormStr(url, (String) null);
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
        UrlBuilder builder = urlBuilder(url, null, httpConfig.getCharset(), httpConfig.isEncode());
        HttpRequest request = getRequest(builder, Method.POST);
        if (null != header) {
            request.addHeaders(header.getHeaders());
        }
        request.body(dataJson, getContentType(Constants.CONTENT_TYPE_JSON_UTF_8));
        return this.exec(request);
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
        UrlBuilder builder = urlBuilder(url, null, httpConfig.getCharset(), httpConfig.isEncode());
        HttpRequest request = getRequest(builder, Method.POST);
        request.formStr(formData);
        request.contentType(getContentType(Constants.CONTENT_TYPE_FORM_DATA_UTF_8));
        if (null != header) {
            request.addHeaders(header.getHeaders());
        }
        return this.exec(request);

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
        UrlBuilder builder = urlBuilder(url, null, httpConfig.getCharset(), httpConfig.isEncode());
        HttpRequest request = getRequest(builder, Method.POST);
        request.form(formData);
        if (null != header) {
            request.addHeaders(header.getHeaders());
        }
        request.header(Constants.CONTENT_TYPE, Constants.MULTIPART);
        return this.exec(request);
    }

    private SimpleHttpResponse exec(HttpRequest request) {
        try (HttpResponse response = request.execute()) {
            return SimpleHttpResponse.builder()
                .success(response.isOk())
                .body(response.bodyBytes())
                .headers(response.headers())
                .build();
        } catch (Exception e) {
            throw new com.hb0730.https.exception.HttpException("request error:" + e.getMessage());
        }
    }

    private HttpRequest getRequest(UrlBuilder url, Method method) {
        return getHttpRequest(url, method, getHttpConfig() == null ? HttpConfig.builder().build() : getHttpConfig(), getHeader());
    }

    private String getContentType(String defaultContentType) {
        if (StringUtils.isBlank(this.httpConfig.getContentType())) {
            return defaultContentType;
        } else {
            return this.httpConfig.getContentType();
        }
    }
}
