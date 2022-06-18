package com.hb0730.https.support.okhttp3;

import cn.hutool.core.io.FileUtil;
import com.hb0730.https.inter.SimpleHttp;
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
}
