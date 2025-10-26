package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.model.UserRequestDTO;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
//      user.setId(userRequestDTO.getId());
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public boolean updateUser(Long id, UserRequestDTO userRequestDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
//          existingUser.setId(userRequestDTO.getId());
            existingUser.setName(userRequestDTO.getName());
            existingUser.setEmail(userRequestDTO.getEmail());
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    public boolean deleteUser(Long id) {
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

}
