package model.dao;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.myapplication.connection.ConnectionPool;
import com.example.myapplication.exception.DaoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import model.entity.Role;
import model.entity.User;

public class SimpleUserDao implements UserDao {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleUserDao.class);
    private static final String SQL_ADD_USER = "insert into user(user_login, user_password, user_role_id) values (?,?,?)";
    private static final String SQL_FIND_USER_BY_LOGIN = "select user_id, user_login, user_password, r.role_name  from  user " +
            "left join role r on r.role_id = user.user_role_id " +
            "where user_login = ?";
    private final ConnectionPool connectionPool;

    public SimpleUserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public User addUser(User user) throws DaoException {
        try (final Connection connection = connectionPool.getConnection(); final PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUserLogin());
            preparedStatement.setString(2, user.getUserPassword());
            preparedStatement.setLong(3, user.getUserRole().ordinal());
            final int countCreatedRows = preparedStatement.executeUpdate();
            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (countCreatedRows > 0 && generatedKeys.next()) {
                return new User.Builder().
                        withUserId(generatedKeys.getLong(1)).
                        withUserLogin(user.getUserLogin()).
                        withUserPassword(user.getUserPassword()).
                        withUserRole(user.getUserRole()).
                        build();
            }
        } catch (SQLException sqlException) {
            LOG.error("Cannot add user userLogin: " + user.getUserLogin() + " userPassword: " + user.getUserPassword() + " userRole: " + user.getUserRole(), sqlException);
            throw new DaoException("Cannot add user userLogin: " + user.getUserLogin() + " userPassword: " + user.getUserPassword() + " userRole: " + user.getUserRole());
        }
        LOG.error("Cannot add user userLogin: " + user.getUserLogin() + " userPassword: " + user.getUserPassword() + " userRole: " + user.getUserRole());
        throw new DaoException("Cannot add user userLogin: " + user.getUserLogin() + " userPassword: " + user.getUserPassword() + " userRole: " + user.getUserRole());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Optional<User> findUserByLogin(String login) throws DaoException {
        try (final Connection connection = connectionPool.getConnection(); final PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User.Builder().
                        withUserId(resultSet.getLong(1)).
                        withUserLogin(resultSet.getString(2)).
                        withUserPassword(resultSet.getString(3)).
                        withUserRole(Role.valueOf(resultSet.getString(4))).
                        build());
            }
        } catch (SQLException sqlException) {
            LOG.error("Cannot find user by login, login: " + login, sqlException);
            throw new DaoException("Cannot find user by login, login: " + login, sqlException);
        }
        LOG.error("Cannot find user by login, login: " + login);
        return Optional.empty();
    }
}
