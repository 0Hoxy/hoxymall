package com.hoxy.hoxymall.util;

import org.apache.commons.lang3.RandomStringUtils;

public class SkuGenerator {

    public static String generateSku(int length) {
        if (length < 8 || length > 12) {
            throw new IllegalArgumentException("길이가 8자 이상 12자 이하여야 합니다.");
        }
        return RandomStringUtils.random(length, true, true);
    }
}
