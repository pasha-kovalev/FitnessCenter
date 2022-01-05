package com.epam.jwd.fitness_center.model.validator;

public final class NumberValidator {
    private static final String NUMBER_REGEX = "\\d+";

    private NumberValidator() {
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
