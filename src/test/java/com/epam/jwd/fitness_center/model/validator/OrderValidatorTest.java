package com.epam.jwd.fitness_center.model.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderValidatorTest {
    public static Object[][] periodTestData() {
        return new Object[][]{
                {1, true},
                {3, true},
                {7, false},
                {0, false},
                {-1, false}
        };
    }

    public static Object[][] cardTestData() {
        return new Object[][]{
                {"1111222233334444", true},
                {"111122223333444", false},
                {"11112222333344444", false},
                {"aaaabbbbccccdddd", false}
        };
    }

    @ParameterizedTest
    @MethodSource("periodTestData")
    void isValidPeriod(long period, boolean expected) {
        boolean result = OrderValidator.isValidPeriod(period);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("cardTestData")
    void isValidCardNumber(String cardNumber, boolean expected) {
        boolean result = OrderValidator.isValidCardNumber(cardNumber);
        assertEquals(expected, result);
    }
}