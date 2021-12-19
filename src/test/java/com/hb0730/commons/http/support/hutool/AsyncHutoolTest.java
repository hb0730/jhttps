package com.hb0730.commons.http.support.hutool;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import org.junit.Test;

/**
 * hutool 异步测试
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2021/12/19
 * @since 3.0.0
 */
public class AsyncHutoolTest {

    @Test
    public void test() {
        HttpResponse response = HttpRequest.get("https://api.apiopen.top/getJoke").executeAsync();
        boolean ok = response.isOk();
        Console.print(ok);
        String body = IoUtil.readUtf8(response.bodyStream());
        response.close();
        Console.print(body);
    }
}
