package com.hb0730.https.support.hutool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import com.hb0730.https.support.SimpleHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HutoolImplTest {
    SimpleHttp http = new HutoolImpl();

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
    public void uploadBytesTest() {
        http.postFile("http://localhost:8080/upload", "file", "test1.txt",
                FileUtil.readBytes(FileUtil.file("test1.txt")));
    }


    @Test
    public void interceptorTest() {
        HttpRequest httpRequest = HttpRequest.get("https://www.baidu.com")
                .addRequestInterceptor(httpObj -> {
                    // 增加请求头
                    System.out.println("请求前");
                    System.out.println(httpObj);
                })
                .addResponseInterceptor((httpObj) -> {
                    // 增加响应头
                    System.out.println("响应后");
                    System.out.println(httpObj);
                });
        HutoolImpl request = new HutoolImpl(httpRequest);
        SimpleHttpResponse response = request.get("https://www.google.com");
        System.out.println(response.getBodyStr());
    }
}
