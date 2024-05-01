package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.core.enums.Role;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(
        connection = EmbeddedDatabaseConnection.H2,
        replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private Role role;
    private User user1, user2;

    @Before
    public void setup() {

        role = Role.USER;
        user1 = User.builder()
                .email("user1@gmail.com")
                .password("test123")
                .firstname("test_f1")
                .lastname("test_l1")
                .role(role)
                .build();
        user2 = User.builder()
                .email("user2@gmail.com")
                .password("test321")
                .firstname("test_f2")
                .lastname("test_l2")
                .role(role)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

    }

    @Test
    public void UserRepository_findByEmail_ReturnsOptionalUser() {

        Optional<User> userOptional = userRepository.findByEmail(user1.getEmail());

        Assert.assertTrue(userOptional.isPresent());
        User fetchedUser = userOptional.get();
        assertEquals(user1, fetchedUser);
    }

    @Test
    public void UserRepository_findByEmail_ReturnsEmptyOptional() {

        Optional<User> userOptional = userRepository.findByEmail("nonexistent@gmail.com");

        Assert.assertFalse(userOptional.isPresent());
    }

    @Test
    public void UserRepository_getUserByEmail_ReturnsOptionalUser() {

        // When
        Optional<User> userOptional = userRepository.getUserByEmail(user1.getEmail());

        // Then
        Assert.assertTrue(userOptional.isPresent());
        User fetchedUser = userOptional.get();
        assertEquals(user1, fetchedUser);
    }

    @Test
    public void UserRepository_getUserByEmail_ReturnsEmptyOptional() {

        Optional<User> userOptional = userRepository.getUserByEmail("nonexistent@gmail.com");

        Assert.assertFalse(userOptional.isPresent());
    }

    @Test
    public void UserRepository_getAllByRole_ReturnsUsersWithRole() {

        userRepository.saveAll(List.of(user1, user2));

        // When
        List<User> users = userRepository.getAllByRole(role);

        // Then
        assertEquals(2, users.size());
        assertEquals(user1.getEmail(), users.get(0).getEmail());
        assertEquals(user2.getEmail(), users.get(1).getEmail());
    }

}
