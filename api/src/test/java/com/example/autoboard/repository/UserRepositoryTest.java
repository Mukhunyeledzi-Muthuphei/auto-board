// package com.example.autoboard.repository;

// import com.example.autoboard.entity.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest
// public class UserRepositoryTest {

//     @Autowired
//     private UserRepository userRepository;

//     private User user;

//     @BeforeEach
//     public void setUp() {
//         user = new User();
//         user.setId("1");
//         user.setFirstName("John");
//         user.setLastName("Doe");
//     }

//     @Test
//     public void testSaveUser() {
//         User savedUser = userRepository.save(user);
//         assertNotNull(savedUser);
//         assertEquals(user.getId(), savedUser.getId());
//         assertEquals(user.getFirstName(), savedUser.getFirstName());
//         assertEquals(user.getLastName(), savedUser.getLastName());
//     }

//     @Test
//     public void testFindUserById() {
//         userRepository.save(user);
//         Optional<User> foundUser = userRepository.findById(user.getId());
//         assertTrue(foundUser.isPresent());
//         assertEquals(user.getId(), foundUser.get().getId());
//     }

//     @Test
//     public void testFindUserByFirstName() {
//         userRepository.save(user);
//         User foundUser = userRepository.findByFirstName(user.getFirstName());
//         assertNotNull(foundUser);
//         assertEquals(user.getFirstName(), foundUser.getFirstName());
//     }

//     @Test
//     public void testFindUserByLastName() {
//         userRepository.save(user);
//         User foundUser = userRepository.findByLastName(user.getLastName());
//         assertNotNull(foundUser);
//         assertEquals(user.getLastName(), foundUser.getLastName());
//     }

//     @Test
//     public void testDeleteUser() {
//         userRepository.save(user);
//         userRepository.delete(user);
//         Optional<User> deletedUser = userRepository.findById(user.getId());
//         assertFalse(deletedUser.isPresent());
//     }
// }