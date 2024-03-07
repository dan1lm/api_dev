package net.javaguides.springboot.service;

import net.javaguides.springboot.dto.UserDTO;
import net.javaguides.springboot.entity.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO user);
    UserDTO getUserByID(Long userID);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(UserDTO user);
    void deleteUser(Long userID);
}
