package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IUserDao;
import com.example.bookshopwebapplication.dao.mapper.ProductMapper;
import com.example.bookshopwebapplication.dao.mapper.UserMapper;
import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.entities.User;

import java.util.List;
import java.util.Optional;

public class UserDao extends AbstractDao<User> implements IUserDao {
    public Long save(User user) {
        // set new Query
        clearSQL();
        builderSQL.append("INSERT INTO user (username, password, fullname, email, phoneNumber, ");
        builderSQL.append("gender, address, role) ");
        builderSQL.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        return insert(builderSQL.toString(), user.getUsername(), user.getPassword(), user.getFullName(),
                user.getEmail(), user.getPhoneNumber(), user.getGender(), user.getAddress(), user.getRole());
    }

    public void update(User user) {
        clearSQL();
        builderSQL.append("UPDATE user SET username = ?, password = ?, fullname = ?, email = ?, ");
        builderSQL.append("phoneNumber = ?, gender = ?, address = ?, role = ?)");
        builderSQL.append("WHERE id = ?");
        update(builderSQL.toString(), user.getUsername(), user.getPassword(), user.getFullName(),
                user.getEmail(), user.getPhoneNumber(), user.getGender(), user.getAddress(), user.getRole(),
                user.getId()
        );
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM user WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    public Optional<User> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM user WHERE id = ?");
        List<User> users = query(builderSQL.toString(), new UserMapper(), id);
        return users.isEmpty() ? null : Optional.ofNullable(users.get(0));
    }

    public List<User> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM user LIMIT " + offset + ", " + limit);
        return getPart(builderSQL.toString(), new UserMapper());
    }

    //int limit, int offset, String orderBy, String sort
    public List<User> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM user ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return super.getOrderedPart(builderSQL.toString(), new UserMapper());
    }

    @Override
    public Optional<User> getByNameUser(String username) {
        clearSQL();
        builderSQL.append("SELECT * FROM user WHERE username = ?");
        List<User> users = query(builderSQL.toString(), new UserMapper(), username);
        return users.isEmpty() ? null : Optional.ofNullable(users.get(0));
    }

    @Override
    public void changePassword(long userId, String newPassword) {
        clearSQL();
        builderSQL.append("UPDATE user SET password = ? WHERE id = ?");
        update(builderSQL.toString(), newPassword, userId);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        clearSQL();
        builderSQL.append("SELECT * FROM user WHERE email = ?");
        List<User> users = query(builderSQL.toString(), new UserMapper(), email);
        return users.isEmpty() ? null : Optional.ofNullable(users.get(0));
    }

    @Override
    public Optional<User> getByPhoneNumber(String phoneNumber) {
        clearSQL();
        builderSQL.append("SELECT * FROM user WHERE phoneNumber = ?");
        List<User> users = query(builderSQL.toString(), new UserMapper(), phoneNumber);
        return users.isEmpty() ? null : Optional.ofNullable(users.get(0));
    }

    public int count() {
        clearSQL();
        builderSQL.append("SELECT COUNT(*) FROM user");
        return count(builderSQL.toString());
    }
}
