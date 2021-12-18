package com.hb0730.commons.http.utils;

import lombok.experimental.UtilityClass;

/**
 * 字符串工具类
 *
 * @author <a></a>
 * @since 3.0.0
 */
@UtilityClass
public class StringUtils {

    /**
     * 是否为{@code  null}
     *
     * @param str 被检测字符
     * @return 是否为 {@code null}
     */
    public boolean isEmpty(String str) {
        return null == str;
    }

    /**
     * 是否为{@link  ""}
     *
     * @param str str
     * @return 是否为 {@link  ""}
     */
    public boolean isBlank(String str) {
        return isEmpty(str) || "".equals(str);
    }

    /**
     * 如果给定字符串{@code str}中不包含{@code appendStr}，则在{@code str}后追加{@code appendStr}；
     * 如果已包含{@code appendStr}，则在{@code str}后追加{@code otherwise}
     *
     * @param str       给定的字符串
     * @param appendStr 需要追加的内容
     * @param otherwise 当{@code appendStr}不满足时追加到{@code str}后的内容
     * @return 追加后的字符串
     */
    public static String appendIfNotContain(final String str, final String appendStr, final String otherwise) {
        if (isEmpty(str) || isEmpty(appendStr)) {
            return str;
        }
        if (str.contains(appendStr)) {
            return str.concat(otherwise);
        }
        return str.concat(appendStr);
    }
}
