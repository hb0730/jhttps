package com.hb0730.https.support;

import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 响应封装
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2022/3/14
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleHttpResponse {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 请求头
     */
    private Map<String, List<String>> headers;
    /**
     * body stream
     */
    private InputStream body;

    /**
     * 获取body，默认UTF-8编码
     *
     * @return body
     * @see #getBody(Charset)
     */
    public String getBody() {
        return this.getBody(StandardCharsets.UTF_8);
    }

    /**
     * 获取body,自动关闭输入流
     *
     * @param charset 编码格式
     * @return body
     */
    public String getBody(Charset charset) {
        return IoUtil.read(this.body, charset);
    }

    public SimpleHttpResponse body(byte[] body) {
        this.body = IoUtil.toStream(body);
        return this;
    }
}
