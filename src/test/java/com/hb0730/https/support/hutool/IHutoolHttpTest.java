package com.hb0730.https.support.hutool;

import cn.hutool.core.net.url.UrlBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class IHutoolHttpTest {

    @Test
    public void urlBuilder() {
        HutoolSyncImpl sync = new HutoolSyncImpl();
        UrlBuilder builder = sync.urlBuilder("http://localhost:8080/test?test=test", null, StandardCharsets.UTF_8, true);
        String params = builder.getQuery().build(StandardCharsets.UTF_8);
        Assert.assertEquals("参数错误", params, "test=test");
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("test2", "test2");
        builder = sync.urlBuilder("http://localhost:8080/test?test=test", queryParams, StandardCharsets.UTF_8, true);
        params = builder.getQuery().build(StandardCharsets.UTF_8);
        Assert.assertEquals("参数错误", params, "test=test&test2=test2");
    }
}
