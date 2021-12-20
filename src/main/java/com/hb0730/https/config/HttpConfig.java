package com.hb0730.https.config;

import com.hb0730.https.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.net.Proxy;

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
     * contentType
     */
    private String contentType = Constants.CONTENT_TYPE_JSON_UTF_8;
}
