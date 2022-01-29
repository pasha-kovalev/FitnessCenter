package com.epam.jwd.fitness_center.model.validator;

import com.epam.jwd.fitness_center.model.entity.User;

public final class UserValidator {
    private static final String PASSWORD_REGEX = "\\w{4,20}";
    private static final String EMAIL_REGEX = "\\w{5,}@\\w+\\.[a-z]{2,4}$";
    private static final String NAME_REGEX = "([А-Яа-я]{1,20})|([A-Z][a-z]{1,20})";

    private UserValidator() {
    }

    public static boolean isValidEmail(String email) {
        return isValidString(email, EMAIL_REGEX);
    }

    public static boolean isValidPassword(String password) {
        return isValidString(password, PASSWORD_REGEX);

    }

    public static boolean isValidName(String name) {
        return isValidString(name, NAME_REGEX);
    }

    public static boolean isEqualPasswords(String password1, String password2) {
        if (password1 == null || password2 == null) {
            return false;
        }
        return password1.equals(password2);
    }

    public static boolean isValidUser(User user, boolean checkPassword) {
        boolean flag = true;
        if (checkPassword) {
            flag = isValidPassword(user.getPassword());
        }
        return isValidEmail(user.getEmail()) && isValidName(user.getFirstName())
                && isValidName(user.getSecondName()) && flag;
    }

    private static boolean isValidString(String str, String regex) {
        if (str == null) {
            return false;
        }
        return str.matches(regex);
    }
}
