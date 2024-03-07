package net.javaguides.springboot.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.springboot.dto.UserDTO;
import net.javaguides.springboot.entity.User;
import net.javaguides.springboot.exception.EmailAlreadyExistsException;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.mapper.AutoUserMapper;
import net.javaguides.springboot.mapper.UserMapper;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // User user = UserMapper.mapToUser(userDTO);
        // User user = modelMapper.map(userDTO, User.class);

        Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());
        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("Email already exists for the user");
        }


        User user = AutoUserMapper.MAPPER.mapToUser(userDTO);

        User savedUser = userRepository.save(user);
        // Convert User JPA entity to UserDto
        // UserDTO savedUserDto = UserMapper.mapToUserDto(savedUser);
        // UserDTO savedUserDto = modelMapper.map(savedUser, UserDTO.class);
        UserDTO savedUserDto = AutoUserMapper.MAPPER.mapToUserDto(savedUser);
        return savedUserDto;
    }

    @Override
    public UserDTO getUserByID(Long userID){
        User user = userRepository.findById(userID).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userID)
        );
        // return UserMapper.mapToUserDto(user);
        // return modelMapper.map(user, UserDTO.class);
        return AutoUserMapper.MAPPER.mapToUserDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        // return users.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
        // return users.stream().map((user) -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
        return users.stream().map((user) -> AutoUserMapper.MAPPER.mapToUserDto(user)).collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(UserDTO user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", user.getId())
        );
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        User updatedUser = userRepository.save(existingUser);
        // return UserMapper.mapToUserDto(updatedUser);
        // return modelMapper.map(updatedUser, UserDTO.class);
        return AutoUserMapper.MAPPER.mapToUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userID) {
        User existingUser = userRepository.findById(userID).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userID)
        );

        userRepository.deleteById(userID);
    }
}