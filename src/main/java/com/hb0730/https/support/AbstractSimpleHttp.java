package com.hb0730.https.support;

import com.hb0730.https.HttpHeader;
import com.hb0730.https.config.HttpConfig;
import lombok.Getter;

/**
 * http 抽象
 *
 * @author bing_huang
 * @since 1.0.0
 */
public abstract class AbstractSimpleHttp implements SimpleHttp {
    protected HttpConfig httpConfig;
    @Getter
    protected HttpHeader header;

    public HttpConfig getHttpConfig() {
        return httpConfig;
    }

    /**
     * set http config
     *
     * @param httpConfig http config
     * @return this
     */
    public AbstractSimpleHttp setHttpConfig(HttpConfig httpConfig) {
        this.httpConfig = httpConfig == null ? HttpConfig.builder().build() : httpConfig;
        return this;
    }

    /**
     * set Http header
     *
     * @param header http header
     * @return this
     */
    public AbstractSimpleHttp setHeader(HttpHeader header) {
        this.header = header;
        return this;
    }

    public AbstractSimpleHttp(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
    }
}
