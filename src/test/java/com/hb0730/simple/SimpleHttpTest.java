package com.hb0730.simple;

import com.hb0730.https.SimpleHttp;
import com.hb0730.https.support.SimpleHttpResponse;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2022/6/18
 * @since 1.0.0
 */
public class SimpleHttpTest {

    @Test
    public void simpleTest() {
        SimpleHttpResponse response = SimpleHttp.HTTP.get("");
    }
}
