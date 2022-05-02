package model.service;

import android.os.Build;

import androidx.annotation.RequiresApi;


import com.example.myapplication.exception.DaoException;
import com.example.myapplication.exception.ServiceError;
import com.example.myapplication.securiy.PasswordHasher;
import com.example.myapplication.validator.UserValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import model.dao.UserDao;
import model.entity.Role;
import model.entity.User;

public class SimpleUserService implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleUserService.class);
    private final UserValidator userValidator;
    private final UserDao userDao;
    private final PasswordHasher passwordHasher;

    public SimpleUserService(UserValidator userValidator, UserDao userDao, PasswordHasher passwordHasher) {
        this.userValidator = userValidator;
        this.userDao = userDao;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User addUserAsClient(String login, String password) {
        if (!userValidator.validateUserDataByLoginAndPassword(login, password)) {
            throw new ServiceError("Invalid user data, userPassword: " + login + " userLogin: " + password + " secretKey: ");
        }
        try {

                final String hashedPassword = passwordHasher.hashPassword(password);
                final User user = new User.Builder().
                        withUserLogin(login).
                        withUserPassword(hashedPassword).
                        withUserRole(Role.CLIENT).
                        build();

                return userDao.addUser(user);

        } catch (DaoException daoException) {
            LOG.error("Cannot add new user, userLogin: " + login + " userPassword: " + password + " secretKey: ", daoException);
            throw new ServiceError("Cannot add new user, userLogin: " + login + " userPassword: " + password + " secretKey: ", daoException);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Optional<User> authenticateIfClient(String login, String password) {
        if (!userValidator.validateUserDataByLoginAndPassword(login, password)) {
            return Optional.empty();
        }
        try {

            final Optional<User> userFromDB = userDao.findUserByLogin(login);
            if (userFromDB.isPresent()) {
                final User userInstance = userFromDB.get();
                final String hashedPasswordFromDB = userInstance.getUserPassword();
                if (userInstance.getUserRole().equals(Role.CLIENT) && passwordHasher.checkIsEqualsPasswordAndPasswordHash(password, hashedPasswordFromDB)) {
                    return userFromDB;
                }
            }
        } catch (DaoException daoException) {
            LOG.error("Cannot authorize user, userLogin: " + login + " userPassword :" + password, daoException);
            throw new ServiceError("Cannot authorize user, userLogin: " + login + " userPassword :" + password);
        }
        return Optional.empty();
    }
}
