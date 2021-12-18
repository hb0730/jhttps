package com.hb0730.commons.http.utils;

import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * 集合工具类
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @since 3.0.0
 */
@UtilityClass
public class CollectionUtils {

    /**
     * 是否为空
     *
     * @param obj obj
     * @return 是否为空
     * @see Map#isEmpty()
     */
    public boolean isEmpty(Map<?, ?> obj) {
        if (null == obj) {
            return true;
        }
        return obj.isEmpty();
    }
}
