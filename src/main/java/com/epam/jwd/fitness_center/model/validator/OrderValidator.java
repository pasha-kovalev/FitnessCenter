package com.epam.jwd.fitness_center.model.validator;

public final class OrderValidator {
    private static final int MIN_PERIOD = 1;
    private static final int MAX_PERIOD = 3;

    public static boolean isValidPeriod(long period) {
        return period >= MIN_PERIOD && period <= MAX_PERIOD;
    }
}
