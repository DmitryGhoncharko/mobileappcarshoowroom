package model.entity;

import java.util.Objects;

public class User {
    private final long userId;
    private final String userLogin;
    private final String userPassword;
    private final String getUserPassword;
    private final Role userRole;

    private User(Builder builder) {
        userId = builder.userId;
        userLogin = builder.userLogin;
        userPassword = builder.userPassword;
        getUserPassword = builder.getUserPassword;
        userRole = builder.userRole;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getGetUserPassword() {
        return getUserPassword;
    }

    public Role getUserRole() {
        return userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (!Objects.equals(userLogin, user.userLogin)) return false;
        if (!Objects.equals(userPassword, user.userPassword)) return false;
        if (!Objects.equals(getUserPassword, user.getUserPassword))
            return false;
        return userRole == user.userRole;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (userLogin != null ? userLogin.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (getUserPassword != null ? getUserPassword.hashCode() : 0);
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userLogin='" + userLogin + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", getUserPassword='" + getUserPassword + '\'' +
                ", userRole=" + userRole +
                '}';
    }

    public static class Builder {
        private long userId;
        private String userLogin;
        private String userPassword;
        private String getUserPassword;
        private Role userRole;

        public Builder withUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withUserLogin(String userLogin) {
            this.userLogin = userLogin;
            return this;
        }

        public Builder withUserPassword(String userPassword) {
            this.userPassword = userPassword;
            return this;
        }

        public Builder withUserRole(Role userRole) {
            this.userRole = userRole;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
