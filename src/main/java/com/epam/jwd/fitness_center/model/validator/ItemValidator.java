package com.epam.jwd.fitness_center.model.validator;

import com.epam.jwd.fitness_center.model.entity.Item;

import java.math.BigDecimal;

public final class ItemValidator {
    private static final String NUMBER_REGEX = "\\d+";

    private ItemValidator() {
    }

    public static boolean isValidItem(Item item) {
        BigDecimal price = item.getPrice();
        String name = item.getName();
        return price != null && name != null && name.length() > 0 && price.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isPositiveInteger(String str) {
        if (str == null || !str.matches(NUMBER_REGEX)) {
            return false;
        }
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return num > 0;
    }
}
