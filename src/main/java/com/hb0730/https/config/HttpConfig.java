package com.hb0730.https.config;

import com.hb0730.https.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.net.Proxy;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Http 配置类
 *
 * @author bing_huang
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpConfig {
    /**
     * 代理配置
     */
    private Proxy proxy;
    /**
     * 超时时长，单位毫秒
     */
    private long timeout = Constants.DEFAULT_TIMEOUT;

    /**
     * 编码, url or form-data
     */
    private boolean encode = false;
    /**
     * charset
     */
    private Charset charset = StandardCharsets.UTF_8;
    /**
     * contentType
     */
    private String contentType;
}
