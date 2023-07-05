package com.hb0730.https.support.httpclient;

import cn.hutool.core.io.FileUtil;
import com.hb0730.https.support.SimpleHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class HttpClientImplTest {

    SimpleHttp http = new HttpClientImpl();

    @Test
    public void uploadFileTest() {
        Map<String, Object> body = new HashMap<>(1);
        body.put("file", FileUtil.file("test1.txt"));
        http.postFormFile("http://localhost:8080/upload", body);
    }

    @Test
    public void uploadFileParmaTest() {
        Map<String, Object> body = new HashMap<>(1);
        body.put("file", FileUtil.file("test1.txt"));
        body.put("name", "test");
        body.put("value", 2);
        http.postFormFile("http://localhost:8080/upload", body);
    }

    @Test
    public void uploadFilesTest() {
        Map<String, Object> body = new HashMap<>(1);
        body.put("file", Arrays.asList(FileUtil.file("test1.txt"), FileUtil.file("test2.txt")));
        body.put("name", "test");
        body.put("value", 2);
        http.postFormFile("http://localhost:8080/upload", body);
    }

    @Test
    public void interceptorTest() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .addInterceptorLast(
                        (HttpRequestInterceptor) (httpRequest, httpContext) -> System.out.println(httpRequest)
                ).addInterceptorLast(
                        (HttpResponseInterceptor) (httpResponse, httpContext) -> System.out.println(httpResponse)
                ).build();
        HttpClientImpl client = new HttpClientImpl(httpClient);
        SimpleHttpResponse response = client.get("https://www.baidu.com");
        String bodyStr = response.getBodyStr();
        System.out.println(bodyStr);
    }
}
