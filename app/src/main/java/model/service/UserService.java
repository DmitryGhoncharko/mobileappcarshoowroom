package model.service;


import java.util.Optional;

import model.entity.User;

public interface UserService {
    User addUserAsClient(String login, String password);

    Optional<User> authenticateIfClient(String login, String password);
}
