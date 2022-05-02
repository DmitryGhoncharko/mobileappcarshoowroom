package model.dao;


import com.example.myapplication.exception.DaoException;

import java.util.Optional;

import model.entity.User;

public interface UserDao {
   User addUser(User user) throws DaoException;

   Optional<User> findUserByLogin(String login) throws DaoException;
}
