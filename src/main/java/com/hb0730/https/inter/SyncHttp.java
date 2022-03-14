package com.hb0730.https.inter;

import com.hb0730.https.HttpHeader;
import com.hb0730.https.support.SimpleHttpResponse;

import java.io.InputStream;
import java.util.Map;

/**
 * sync http  接口
 *
 * @author bing_huang
 * @since 1.0.0
 */
public interface SyncHttp extends Http {

    /**
     * get 请求
     *
     * @param url 请求地址
     * @return 响应结果
     */
    SimpleHttpResponse get(String url);

    /**
     * get 请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    SimpleHttpResponse get(String url, Map<String, String> params);

    /**
     * post请求
     *
     * @param url 请求地址
     * @return 响应结果
     */
    SimpleHttpResponse post(String url);

    /**
     * post请求
     *
     * @param url      请求地址
     * @param dataJson 请求参数，json格式
     * @return 响应结果
     */
    SimpleHttpResponse post(String url, String dataJson);

    /**
     * post请求
     *
     * @param url      请求地址
     * @param dataJson 请求参数，json格式
     * @param header   一次性请求头
     * @return 响应结果
     */
    SimpleHttpResponse post(String url, String dataJson, HttpHeader header);

    /**
     * post请求
     *
     * @param url      请求地址
     * @param formdata form 参数
     * @return 响应结果
     */
    SimpleHttpResponse post(String url, Map<String, String> formdata);

    /**
     * post 请求
     *
     * @param url      请求地址
     * @param formData 表单参数
     * @param header   请求头
     * @return 响应结果
     */
    SimpleHttpResponse post(String url, Map<String, String> formData, HttpHeader header);

}
