package com.hb0730.https.support.okhttp3;

import cn.hutool.core.map.MapBuilder;
import com.hb0730.https.HttpHeader;
import com.hb0730.https.config.HttpConfig;
import com.hb0730.https.constants.Constants;
import com.hb0730.https.inter.AbstractSyncHttp;
import com.hb0730.https.inter.SyncHttp;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class OkHttp3SyncImplTest {
    @Test
    public void okHttp3SyncTest() {
        OkHttp3SyncImpl sync = new OkHttp3SyncImpl();
        Assert.assertNotNull("创建失败", sync);
    }

    @Test
    public void testOkHttp3SyncTest() {
        OkHttp3SyncImpl sync = new OkHttp3SyncImpl(HttpConfig.builder().build());
        Assert.assertNotNull("创建失败", sync);
    }

    @Test
    public void testOkHttp3SyncTest2() {
        OkHttpClient.Builder newBuilder = new OkHttpClient().newBuilder();
        OkHttp3SyncImpl sync = new OkHttp3SyncImpl(newBuilder, HttpConfig.builder().build());
        Assert.assertNotNull("创建失败", sync);
    }

    @Test
    public void getTest() {
        //无参请求
        SyncHttp sync = new OkHttp3SyncImpl();
        //http://poetry.apiopen.top/getTime
        String result = sync.get("");
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void getTestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        OkHttp3SyncImpl okHttp3Sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        String result = okHttp3Sync.get("http://localhost:8089/");
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testGetTest() {
        //有参请求
        SyncHttp sync = new OkHttp3SyncImpl();
        MapBuilder<String, String> build = MapBuilder.create();
        Map<String, String> params = build.put("count", "2").build();
        //http://poetry.apiopen.top/getTime
        String result = sync.get("", params);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testGetTestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        MapBuilder<String, String> build = MapBuilder.create();
        Map<String, String> params = build.put("test", "中文").build();
        OkHttp3SyncImpl okHttp3Sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        String result = okHttp3Sync.get("http://localhost:8089/params", params);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testGet2Test() {
        //多参请求
        SyncHttp sync = new OkHttp3SyncImpl();
        MapBuilder<String, String> build = MapBuilder.create();
        Map<String, String> params = build.put("count", "2").build();
        //http://poetry.apiopen.top/poetryFull?page=1
        String result = sync.get("", params);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testGet2TestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        MapBuilder<String, String> build = MapBuilder.create();
        Map<String, String> params = build.put("count", "2").put("test", "中文").build();
        OkHttp3SyncImpl okHttp3Sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        String result = okHttp3Sync.get("http://localhost:8089/params", params);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testGet2TestCustomerClient2() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        MapBuilder<String, String> build = MapBuilder.create();
        Map<String, String> params = build.put("count", "2").put("test", "中文").build();
        OkHttp3SyncImpl okHttp3Sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().encode(true).build());
        String result = okHttp3Sync.get("http://localhost:8089/params", params);
        Assert.assertNotNull("获取失败", result);
    }


    @Test
    public void testGet1Test() {
        //请求头 参数判断
        AbstractSyncHttp sync = new OkHttp3SyncImpl();
        MapBuilder<String, String> build = MapBuilder.create();
        Map<String, String> params = build
            .put("count", "2")
            .put("page", "1")
            .put("name", "李白")
            .build();
        HttpHeader httpHeader = HttpHeader.builder();
        httpHeader.add("Accept", "*/*");
        //测试为空,为null
        httpHeader.add("ab", "");
        httpHeader.add("ac", null);
        //http://poetry.apiopen.top/poetryAuthor
        sync.setHeader(httpHeader);
        String result = sync.get("", params);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testGet1TestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        MapBuilder<String, String> build = MapBuilder.create();
        Map<String, String> params = build
            .put("count", "2")
            .put("page", "1")
            .put("name", "李白")
            .build();
        HttpHeader httpHeader = HttpHeader.builder();
        httpHeader.add("Accept", "*/*");
        //测试为空,为null
        httpHeader.add("ab", "aa");
        httpHeader.add("ac", "xx");
        OkHttp3SyncImpl okHttp3Sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        okHttp3Sync.setHeader(httpHeader);
        String result = okHttp3Sync.get("http://localhost:8089/params/header", params);
        Assert.assertNotNull("获取失败", result);
    }


    @Test
    public void testGet3Test() {
        //请求头 参数判断
        AbstractSyncHttp sync = new OkHttp3SyncImpl();
        sync.setHttpConfig(HttpConfig.builder().encode(true).build());
        MapBuilder<String, String> build = MapBuilder.create();
        Map<String, String> params = build
            .put("count", "2")
            .put("page", "1")
            .put("name", "李白")
            .build();
        HttpHeader httpHeader = HttpHeader.builder();
        httpHeader.add("Accept", "*/*");
        //测试为空,为null
        httpHeader.add("ab", "");
        httpHeader.add("ac", null);
        //http://poetry.apiopen.top/poetryAuthor
        sync.setHeader(httpHeader);
        String result = sync.get("", params);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void postTest() {
        AbstractSyncHttp sync = new OkHttp3SyncImpl();
        //http://meiriyikan.cn/api/json.php
        String result = sync.post("");
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void postTestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        AbstractSyncHttp sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        sync.setHttpConfig(HttpConfig.builder().encode(true).build());
        String result = sync.post("http://localhost:8089");
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testPostTest() {
        AbstractSyncHttp sync = new OkHttp3SyncImpl();
        String json = "{\"name:\",\"李白\"}";
        // https://api.apiopen.top/likePoetry
        String result = sync.post("", json);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testPostTestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        AbstractSyncHttp sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        String json = "{\"name:\",\"李白\"}";
        sync.setHttpConfig(HttpConfig.builder().encode(false).contentType(Constants.CONTENT_TYPE_JSON_UTF_8).build());
        String result = sync.post("http://localhost:8089/body", json);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testPost1Test() {
        AbstractSyncHttp sync = new OkHttp3SyncImpl();
        MapBuilder<String, String> mapBuilder = MapBuilder.create();
        mapBuilder.put("type", "all");
        mapBuilder.put("page", "1");
        mapBuilder.put("count", "10");
        // https://api.apiopen.top/getJoke
        String result = sync.post("", mapBuilder.build());
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testPost1TestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        AbstractSyncHttp sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        MapBuilder<String, String> mapBuilder = MapBuilder.create();
        mapBuilder.put("type", "all");
        mapBuilder.put("page", "1");
        mapBuilder.put("count", "10");
        sync.setHttpConfig(HttpConfig.builder().encode(false).contentType(Constants.CONTENT_TYPE_FORM_DATA_UTF_8).build());
        String result = sync.post("http://localhost:8089/formdata", mapBuilder.build());
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testPost2Test() {
        AbstractSyncHttp sync = new OkHttp3SyncImpl();
        String json = "{\"name:\",\"李白\"}";
        // https://api.apiopen.top/likePoetry
        HttpHeader header = HttpHeader.builder().add("Accept", "*/*");
        sync.setHeader(header);
        String result = sync.post("", json);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testPost2TestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        AbstractSyncHttp sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        String json = "{\"name:\",\"李白\"}";
        sync.setHttpConfig(HttpConfig.builder().encode(false).contentType(Constants.CONTENT_TYPE_JSON_UTF_8).build());
        HttpHeader header = HttpHeader.builder().add("Accept", "*/*");
        sync.setHeader(header);
        String result = sync.post("http://localhost:8089/body/header", json);
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testPost3Test() {
        AbstractSyncHttp sync = new OkHttp3SyncImpl();
        MapBuilder<String, String> mapBuilder = MapBuilder.create();
        mapBuilder.put("type", "all");
        mapBuilder.put("page", "1");
        mapBuilder.put("count", "10");
        // https://api.apiopen.top/getJoke
        HttpHeader header = HttpHeader.builder().add("Accept", "*/*");
        sync.setHeader(header);
        String result = sync.post("", mapBuilder.build());
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void testPost3TestCustomerClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());
        AbstractSyncHttp sync = new OkHttp3SyncImpl(builder, HttpConfig.builder().build());
        MapBuilder<String, String> mapBuilder = MapBuilder.create();
        mapBuilder.put("type", "all");
        mapBuilder.put("page", "1");
        mapBuilder.put("count", "10");
        mapBuilder.put("test", "中午");
        HttpHeader header = HttpHeader.builder().add("Accept", "*/*");
        sync.setHeader(header);
        sync.setHttpConfig(HttpConfig.builder().encode(true).contentType(Constants.CONTENT_TYPE_FORM_DATA_UTF_8).build());
        String result = sync.post("http://localhost:8089/formdata/header", mapBuilder.build());
        Assert.assertNotNull("获取失败", result);
    }

    public class LoggingInterceptor implements Interceptor {
        public static final String CONTENT_ENCODING = "content-encoding";

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            String content = null;
            ResponseBody body = response.body();
            if (body != null) {
                MediaType contentType = body.contentType();
                String contentEncoding = response.header(CONTENT_ENCODING);
                if ("gzip".equals(contentEncoding)) {
                    BufferedSource buffer = Okio.buffer(new GzipSource(body.source()));
                    content = buffer.readUtf8();
                    ResponseBody wrappedBody = ResponseBody.create(contentType, content);
                    response = response.newBuilder().removeHeader(CONTENT_ENCODING).body(wrappedBody).build();
                } else {
                    content = body.string();
                    ResponseBody wrappedBody = ResponseBody.create(contentType, content);
                    response = response.newBuilder().body(wrappedBody).build();
                }
            }
            String protocol = response.protocol().name().replaceFirst("_", "/");
            protocol = protocol.replace('_', '.');
            String httpLine = "" + protocol + ' ' + response.code();
            System.out.println("OkHttp:" + String.format("--> Sending request %s\n%s\n%s\n%s", request.url(),
                request.method(),
                request.body() == null ? "" : request.body().contentType() == null ? "" :
                    request.body().contentType().toString(),
                request.headers()
            ));

            System.out.println("OkHttp:" + String.format("%s\n%s\n\n%s", httpLine, response.headers(), content));

            return response;
        }
    }
}
