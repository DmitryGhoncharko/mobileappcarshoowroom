package com.example.myapplication.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleUserValidator implements UserValidator {

    private static final String LOGIN_REGEXP = "^[a-zA-Z0-9]{6,100}$";

    private static final String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,100}$";

//    private static final String SECRET_KEY_REGEXP = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{10,100}$";

    @Override
    public boolean validateUserDataByLoginAndPassword(String login, String password) {
        if (login != null && password != null) {
            Pattern pattern = Pattern.compile(LOGIN_REGEXP);
            Matcher userLoginMather = pattern.matcher(login);
            final boolean userLoginIsValid = userLoginMather.find();
            pattern = Pattern.compile(PASSWORD_REGEXP);
            Matcher userPasswordMatcher = pattern.matcher(password);
            final boolean userPasswordIsValid = userPasswordMatcher.find();
            return userLoginIsValid && userPasswordIsValid;
        }
        return false;
    }

    @Override
    public boolean validateUserDataByLoginAndPasswordWithSecretKey(String login, String password, String secretKey) {
        if (secretKey != null) {
//            final Pattern pattern = Pattern.compile(SECRET_KEY_REGEXP);
//            final Matcher matcher = pattern.matcher(secretKey);
//            final boolean secretKeyIsValid = matcher.find();
            return validateUserDataByLoginAndPassword(login, password);
        }
        return false;
    }
}
