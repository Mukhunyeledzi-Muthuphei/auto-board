package com.example.autoboard.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("1", user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());

        user.setFirstName("Jane");
        user.setLastName("Smith");

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
    }

    @Test
    public void testUserEquality() {
        User anotherUser = new User();
        anotherUser.setId("1");
        anotherUser.setFirstName("John");
        anotherUser.setLastName("Doe");

        assertEquals(user, anotherUser);
    }

    @Test
    public void testUserHashCode() {
        User anotherUser = new User();
        anotherUser.setId("1");
        anotherUser.setFirstName("John");
        anotherUser.setLastName("Doe");

        assertEquals(user.hashCode(), anotherUser.hashCode());
    }
}