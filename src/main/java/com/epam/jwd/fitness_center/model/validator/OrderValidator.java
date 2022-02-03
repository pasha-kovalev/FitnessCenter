package com.epam.jwd.fitness_center.model.validator;

public final class OrderValidator {
    private static final int MIN_PERIOD = 1;
    private static final int MAX_PERIOD = 3;
    private static final String CARD_NUMBER_REGEX = "[0-9]{16}";

    public static boolean isValidPeriod(long period) {
        return period >= MIN_PERIOD && period <= MAX_PERIOD;
    }

    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        return cardNumber.matches(CARD_NUMBER_REGEX);
    }
}
