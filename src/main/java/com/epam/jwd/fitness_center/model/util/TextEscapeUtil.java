package com.epam.jwd.fitness_center.model.util;

import org.apache.commons.text.StringEscapeUtils;
public final class TextEscapeUtil {
    private TextEscapeUtil() {
    }

    public static String escapeHtml(String text) {
        return StringEscapeUtils.escapeHtml4(text);
    }
}
