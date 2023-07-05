package com.hb0730.https.support.okhttp3;

import cn.hutool.core.io.FileUtil;
import com.hb0730.https.support.SimpleHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class OkHttp3ImplTest {
    SimpleHttp http = new OkHttp3Impl();

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
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    System.out.println("请求前");
                    System.out.println(request);
                    return chain.proceed(request);
                });
        OkHttp3Impl okHttp3 = new OkHttp3Impl(clientBuilder);
        SimpleHttpResponse response = okHttp3.get("https://www.baidu.com");
        System.out.println(response.getBodyStr());
    }
}
