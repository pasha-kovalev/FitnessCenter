package com.epam.jwd.fitness_center.model.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserValidatorTest {
    public static Object[][] emailTestData() {
        return new Object[][]{
                {"kova.aa_sf@mail.ru", true},
                {"kova.aa_sf@mail.r", false},
                {"kovaa_smail.ru", false}
        };
    }

    public static Object[][] passwordTestData() {
        return new Object[][]{
                {"Asdf1", false},
                {"Asdfghj1", true},
                {"asdfgh1", false},
                {"Asdfghj", false},
                {"Индийскийчай228", false}
        };
    }

    public static Object[][] nameTestData() {
        return new Object[][]{
                {"Ye", true},
                {"Паша", true},
                {"Donovan", true},
                {"Ab0bus", false},
                {"Chikibamb()ni", false}
        };
    }

    public static Object[][] passwordsTestData() {
        return new Object[][]{
                {"Asdfghj1", "Asdfghj1", true},
                {"Asdfghj1", "Аsdfghj1", false}, //Cyrillic A in the second
                {"Semka777", "Semka77", false}
        };
    }

    @ParameterizedTest
    @MethodSource("emailTestData")
    void isValidEmail(String email, boolean expected) {
        boolean result = UserValidator.isValidEmail(email);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("passwordTestData")
    void isValidPassword(String password, boolean expected) {
        boolean result = UserValidator.isValidPassword(password);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("nameTestData")
    void isValidName(String name, boolean expected) {
        boolean result = UserValidator.isValidName(name);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("passwordsTestData")
    void isEqualPasswords(String password1, String password2, boolean expected) {
        boolean result = UserValidator.isEqualPasswords(password1, password2);
        assertEquals(expected, result);
    }
}