package com.hb0730.https;

import com.hb0730.https.support.callback.HttpCallback;
import com.hb0730.https.support.httpclient.HttpClientSyncImpl;
import com.hb0730.https.support.okhttp3.OkHttp3SyncImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class HttpsTest {

    @Test
    public void syncClientTest() {
        HttpSync http = Https.SYNC.getHttp();
        http.setHttp(new HttpClientSyncImpl());
        //http://poetry.apiopen.top/getTime
        String result = http.get("");
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void syncOkHttp3Test() {
        HttpSync http = Https.SYNC.getHttp();
        http.setHttp(new OkHttp3SyncImpl());
        //http://poetry.apiopen.top/getTime
        String result = http.get("");
        Assert.assertNotNull("获取失败", result);
    }

    @Test
    public void asyncTest() {
        HttpAsync async = Https.ASYNC.getHttp();
        //http://poetry.apiopen.top/getTime
        async.get("", new HttpCallback() {
            @Override
            public void success(String result) throws IOException {

                Assert.assertNotNull("获取失败", result);
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }
}