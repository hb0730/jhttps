package com.hb0730.https.utils;

import com.hb0730.https.HttpAsync;
import com.hb0730.https.HttpSync;
import com.hb0730.https.Https;
import com.hb0730.https.support.callback.HttpCallback;
import com.hb0730.https.support.okhttp3.OkHttp3SyncImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpsTest {

    @Test
    public void testSync() {
        HttpSync http = Https.SYNC.getHttp();
        String s = http.get("https://baidu.com");
        log.debug(s);
        s = http.setHttp(new OkHttp3SyncImpl())
                .get("https://baidu.com");
        log.debug(s);
    }

    @Test
    public void testAsync() throws InterruptedException {
        HttpAsync http = Https.ASYNC.getHttp();
        http.get("http://baidu.com", new HttpCallback() {
            @Override
            public void success(String result) throws IOException {
                log.debug(result);
            }

            @Override
            public void failure(Exception e) {

            }
        });
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));
    }
}
