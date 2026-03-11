package org.example.backendpractice.service;

import org.example.backendpractice.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(int id);

    void save(User user);

    void deleteById(int id);
}
