package com.hb0730.https.support.okhttp3;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.exception.HttpException;
import com.hb0730.https.utils.MapUtils;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Okhttp3 抽象
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @since 3.0.0
 */
public interface IOkhttp3 {
    MediaType JSON_UTF_8 = MediaType.parse(Constants.CONTENT_TYPE_JSON_UTF_8);
    MediaType FORM_DATA_UTF_8 = MediaType.parse(Constants.CONTENT_TYPE_FORM_DATA_UTF_8);

    /**
     * 构建 get请求
     *
     * @param url        url
     * @param params     params
     * @param urlEnabled 是否编码
     * @param header     请求头
     * @return Request Build
     */
    default Request.Builder getRequestBuilder(String url, Map<String, String> params, boolean urlEnabled, Map<String,
        String> header) {
        HttpUrl httpUrl = buildHttpUrl(url, params, urlEnabled);
        Request.Builder requestBuilder = new Request.Builder().url(httpUrl);
        if (null != header) {
            MapUtils.forEach(header, requestBuilder::addHeader);
        }
        return requestBuilder.get();
    }

    /**
     * 构建 post application/json请求
     *
     * @param url         url
     * @param dataJson    body
     * @param contentType content-type
     * @param header      headers
     * @return request builder
     */
    default Request.Builder postJsonRequestBuild(String url, String dataJson, MediaType contentType, Map<String,
        String> header) {
        HttpUrl httpUrl = buildHttpUrl(url, null, true);
        Request.Builder builder = new Request.Builder().url(httpUrl);
        if (null != header) {
            MapUtils.forEach(header, builder::addHeader);
        }
        builder.addHeader("Content-Type", contentType.toString());
        RequestBody body = RequestBody.create(dataJson, contentType);

        return builder.post(body);
    }

    /**
     * build post form-data request
     *
     * @param url         url
     * @param formdata    form-data
     * @param encode      是否编码
     * @param contentType 类型，只取charset
     * @param headers     请求头
     * @return form-data request build
     */
    default Request.Builder postFormDataRequestBuild(String url, Map<String, String> formdata,
                                                     boolean encode, MediaType contentType,
                                                     Map<String, String> headers) {
        HttpUrl httpUrl = buildHttpUrl(url, null, true);
        Request.Builder builder = new Request.Builder().url(httpUrl);
        if (null != headers) {
            MapUtils.forEach(headers, builder::addHeader);
        }
        builder.addHeader("Content-Type", contentType.toString());
        FormBody.Builder formBuild = new FormBody.Builder(contentType.charset());
        if (encode) {
            MapUtils.forEach(formdata, formBuild::addEncoded);
        } else {
            MapUtils.forEach(formdata, formBuild::add);
        }
        FormBody formBody = formBuild.build();
        return builder.post(formBody);
    }

    default Request.Builder postFormFileRequestBuild(String url, Map<String, Object> formdata, Map<String,
        String> headers) {
        HttpUrl httpUrl = buildHttpUrl(url, null, true);
        Request.Builder builder = new Request.Builder().url(httpUrl);
        if (null != headers) {
            MapUtils.forEach(headers, builder::addHeader);
        }
        builder.addHeader(Constants.CONTENT_TYPE, Constants.MULTIPART);
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        for (Map.Entry<String, Object> entry : formdata.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File) {
                multipartBody.addFormDataPart(
                    name,
                    ((File) value).getName(),
                    RequestBody.create(
                        (File) value,
                        MediaType.parse(FileUtil.getMimeType(((File) value).toPath()))
                    )
                );
                continue;
            }
            String strValue;
            if (value instanceof Iterable) {
                // 列表对象
                strValue = CollUtil.join((Iterable<?>) value, ",");
            } else if (ArrayUtil.isArray(value)) {
                if (File.class == ArrayUtil.getComponentType(value)) {
                    File[] files = (File[]) value;
                    for (int i = 0; i < files.length; i++) {
                        MultipartBody.Part part = MultipartBody.Part.createFormData(
                            name + i, files[i].getName(), RequestBody.create(files[i],
                                MediaType.parse(FileUtil.getMimeType(files[i].getName()))));
                        multipartBody.addPart(part);
                    }
                    continue;
                }
                // 数组对象
                strValue = ArrayUtil.join((Object[]) value, ",");
            } else {
                // 其他对象一律转换为字符串
                strValue = Convert.toStr(value, null);
            }
            multipartBody.addFormDataPart(name, strValue);
        }
        MultipartBody body = multipartBody.build();
        return builder.post(body);
    }

    /**
     * 构建http url
     *
     * @param url        请求url
     * @param params     url参数
     * @param urlEnabled 是否编码
     * @return http url
     */
    default HttpUrl buildHttpUrl(String url, Map<String, String> params, boolean urlEnabled) {
        HttpUrl parse = HttpUrl.parse(url);
        if (null == parse) {
            throw new HttpException("url missing");
        }
        HttpUrl.Builder builder = parse.newBuilder();
        if (urlEnabled) {
            MapUtils.forEach(params, builder::addEncodedQueryParameter);
        } else {
            MapUtils.forEach(params, builder::addQueryParameter);
        }
        return builder.build();
    }

    /**
     * 构建 OkHttpClient
     *
     * @param clientBuilder okHttpClient build
     * @param config        client config
     * @return OkHttpClient
     */
    default OkHttpClient buildClient(final OkHttpClient.Builder clientBuilder, HttpConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (clientBuilder != null) {
            builder = clientBuilder;
        }
        builder.connectTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        builder.proxy(config.getProxy());
        builder.readTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        builder.writeTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        return builder.build();
    }
}
