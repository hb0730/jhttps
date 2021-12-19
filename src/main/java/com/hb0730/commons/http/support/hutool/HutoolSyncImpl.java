package com.hb0730.commons.http.support.hutool;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.hb0730.commons.http.config.HttpConfig;
import com.hb0730.commons.http.constants.Constants;
import com.hb0730.commons.http.inter.AbstractSyncHttp;
import com.hb0730.commons.http.utils.MapUtils;
import com.hb0730.commons.http.utils.StringUtils;

import java.util.Map;

/**
 * hutool http
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2021/12/18
 * @since 3.0.0
 */
public class HutoolSyncImpl extends AbstractSyncHttp {
    public HutoolSyncImpl() {
        this(HttpConfig.builder().build());
    }

    public HutoolSyncImpl(HttpConfig httpConfig) {
        super(httpConfig);
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
        String baseUrl = StringUtils.appendIfNotContain(url, "?", "&");
        baseUrl = baseUrl + MapUtils.parseMapToUrlString(params, this.httpConfig.isEncode());
        HttpRequest request = getRequest(baseUrl, Method.GET);
        HttpResponse execute = request.execute();
        if (execute.isOk()) {
            return execute.body();
        }
        return Constants.EMPTY;

    }

    @Override
    public String post(String url) {
        return this.post(url, (String) null);
    }

    @Override
    public String post(String url, String dataJson) {
        if (StringUtils.isEmpty(url)) {
            return Constants.EMPTY;
        }
        HttpRequest request = getRequest(url, Method.POST);
        request.body(dataJson);
        HttpResponse execute = request.execute();
        if (execute.isOk()) {
            return execute.body();
        }
        return Constants.EMPTY;
    }

    @Override
    public String post(String url, Map<String, String> formdata) {
        if (StringUtils.isEmpty(url)) {
            return Constants.EMPTY;
        }
        HttpRequest request = getRequest(url, Method.POST);
        request.formStr(formdata);
        HttpResponse execute = request.execute();
        if (execute.isOk()) {
            return execute.body();
        }
        return Constants.EMPTY;
    }

    private HttpRequest getRequest(String url, Method method) {
        HttpRequest request = new HttpRequest(url).setMethod(method);
        HttpConfig config = getHttpConfig();
        request.setProxy(config.getProxy());
        request.setConnectionTimeout(Math.toIntExact(config.getTimeout()));
        if (null != getHeader()) {
            request.addHeaders(getHeader().getHeaders());
        }
        return request;
    }
}
