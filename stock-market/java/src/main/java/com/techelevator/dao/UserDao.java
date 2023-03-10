package com.techelevator.dao;

import com.techelevator.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User getUserById(Long userId);

    User findByUsername(String username);

    int findIdByUsername(String username);

    //Edited interface to for first and last
    boolean create(String username, String password, String role, String firstName, String lastName);

    List<User> getAllUsersByGame(int gameId);
}
