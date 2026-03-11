package org.example.backendpractice.service;


import org.example.backendpractice.dao.UserRepository;
import org.example.backendpractice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);

        User user = null;

        if (result.isPresent()) {
            user = result.get();
        }
        else {
            // we didn't find the user
            throw new RuntimeException("Did not find user id - " + id);
        }

        return user;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

}
