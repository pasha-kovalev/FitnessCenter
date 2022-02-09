package com.epam.jwd.fitness_center.model.validator;

import com.epam.jwd.fitness_center.model.entity.Item;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemValidatorTest {
    public static Object[][] itemTestData() {
        return new Object[][]{
                {new Item("Name", BigDecimal.TEN, "Desc"), true},
                {new Item("", BigDecimal.TEN, "Desc"), false},
                {new Item("Name", BigDecimal.ZERO, "Desc"), false},
                {new Item("Name", BigDecimal.valueOf(-1), "Desc"), false}
        };
    }

    @ParameterizedTest
    @MethodSource("itemTestData")
    void isValidItem(Item item, boolean expected) {
        boolean result = ItemValidator.isValidItem(item);
        assertEquals(expected, result);
    }
}