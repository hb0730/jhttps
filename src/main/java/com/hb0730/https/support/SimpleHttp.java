package com.hb0730.https.support;

import com.hb0730.https.HttpHeader;

import java.io.File;
import java.util.Map;

/**
 * http  接口
 *
 * @author bing_huang
 * @since 4.0.0
 */
public interface SimpleHttp {

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
     * post 表单请求
     *
     * @param url      请求地址
     * @param formdata form 参数
     * @return 响应结果
     */
    SimpleHttpResponse postFormStr(String url, Map<String, String> formdata);

    /**
     * post 表单请求
     *
     * @param url      请求地址
     * @param formData 表单参数
     * @param header   请求头
     * @return 响应结果
     */
    SimpleHttpResponse postFormStr(String url, Map<String, String> formData, HttpHeader header);

    /**
     * post multipart/form-data 文件上传
     *
     * @param url       请求地址
     * @param name      参数名称
     * @param filename  文件名称
     * @param fileBytes 需要上传的文件
     * @return 响应
     */
    SimpleHttpResponse postFile(String url, String name, String filename, byte[] fileBytes);

    /**
     * post multipart/form-data 文件上传
     *
     * @param url  请求地址
     * @param name 参数名称
     * @param file 文件
     * @return 响应
     */
    SimpleHttpResponse postFile(String url, String name, File file);

    /**
     * post multipart/form-data 文件上传
     *
     * @param url      url
     * @param formData 文件上传参数
     * @return 响应信息
     */
    SimpleHttpResponse postFormFile(String url, Map<String, Object> formData);

    /**
     * post multipart/form-data 文件上传
     *
     * @param url      url
     * @param formData 文件上传参数
     * @param header   请求头
     * @return 响应信息
     */
    SimpleHttpResponse postFormFile(String url, Map<String, Object> formData, HttpHeader header);
}
