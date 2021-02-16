package com.bitso.challenge.db;

import com.bitso.challenge.model.UserModel;
import com.bitso.challenge.model.entity.User;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Optional;


public class UserModelImpl implements UserModel {

    private Sql2o sql2o;

    public UserModelImpl(String connStr) {
        sql2o = new Sql2o(connStr, null,null);
        createTable();

    }


    @Override
    public Optional<User> get(long id) {
        try (Connection conn = sql2o.open()) {
            User user = conn.createQuery("select * from users where id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> get(String email) {
        try (Connection conn = sql2o.open()) {
            User user = conn.createQuery("select * from users where email = :email")
                    .addParameter("email", email)
                    .executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    public void add(User user) {

        try (Connection conn = sql2o.open()) {
            conn.createQuery("insert into users( email, password) " +
                    "VALUES (:email, :password)")
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword())
                    .executeUpdate();
        }
    }

    private void createTable() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("CREATE TABLE IF NOT EXISTS users (" +
                    "   id INTEGER identity," +
                    "   email varchar(50) NOT NULL," +
                    "   password varchar(500) NOT NULL" +
                    ")"
            ).executeUpdate();
        }
    }
}
