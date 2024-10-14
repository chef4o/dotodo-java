package com.pago.dotodo.common.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomStringUtil extends StringUtils {
    public String normalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return capitalize(str.toLowerCase()).replaceAll("_", "\s");
    }

    public String convertToEnum(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.toUpperCase().replaceAll("\s+", "_");
    }
}
