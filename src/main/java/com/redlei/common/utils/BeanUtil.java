package com.redlei.common.utils;

import org.springframework.beans.BeanUtils;

public class BeanUtil {
    public static <T> T copyProperties(Object source,Class<T> targetClazz) {
        return copyProperties(source, targetClazz, null);
    }

    public static <T> T copyProperties(Object source,Class<T> targetClazz,String... ignoreProperties) {
        try {
            T t = targetClazz.newInstance();
            BeanUtils.copyProperties(source,t,ignoreProperties);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
