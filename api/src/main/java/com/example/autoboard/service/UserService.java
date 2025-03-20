package com.example.autoboard.service;

import com.example.autoboard.entity.User;
import com.example.autoboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Creates a new user in the system.
     * 
     * @param user The user to be created.
     * @return The created user.
     * @deprecated This method is deprecated since version 1.0 and will be
     *             removed
     *             in a future release.
     *             Use a different method for user creation.
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user in the system.
     *
     * @param id          The ID of the user to be updated.
     * @param userDetails The updated user details.
     * @return The updated user.
     * @deprecated This method is deprecated since version 1.0 and will be
     *             removed
     *             in a future release.
     *             Use a different method for user updates.
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public User updateUser(String id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }
}
