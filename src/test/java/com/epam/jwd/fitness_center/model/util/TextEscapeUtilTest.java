package com.epam.jwd.fitness_center.model.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextEscapeUtilTest {
    public static Object[][] escapeTestData() {
        return new Object[][]{
                {"<", "&lt;"},
                {">", "&gt;"},
                {"\"", "&quot;"}
        };
    }

    @ParameterizedTest
    @MethodSource("escapeTestData")
    void escapeHtml(String text, String expected) {
        String result = TextEscapeUtil.escapeHtml(text);
        assertEquals(expected, result);
    }
}