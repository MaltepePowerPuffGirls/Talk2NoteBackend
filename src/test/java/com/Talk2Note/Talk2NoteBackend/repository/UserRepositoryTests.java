package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.core.enums.Role;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(
        connection = EmbeddedDatabaseConnection.H2,
        replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_findByEmail_ReturnsOptionalUser() {
        // Given
        User savedUser = User.builder()
                .email("test@gmail.com")
                .password("test123")
                .firstname("test_f")
                .lastname("test_l")
                .build();
        userRepository.save(savedUser);

        // When
        Optional<User> userOptional = userRepository.findByEmail(savedUser.getEmail());

        // Then
        Assert.assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(savedUser, user);
    }

    @Test
    public void UserRepository_findByEmail_ReturnsEmptyOptional() {
        // Given no user is saved with this email

        // When
        Optional<User> userOptional = userRepository.findByEmail("nonexistent@gmail.com");

        // Then
        Assert.assertFalse(userOptional.isPresent());
    }

    @Test
    public void UserRepository_getUserByEmail_ReturnsOptionalUser() {
        // Given
        User savedUser = User.builder()
                .email("test@gmail.com")
                .password("test123")
                .firstname("test_f")
                .lastname("test_l")
                .build();
        userRepository.save(savedUser);

        // When
        Optional<User> userOptional = userRepository.getUserByEmail(savedUser.getEmail());

        // Then
        Assert.assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertEquals(savedUser, user);
    }

    @Test
    public void UserRepository_getUserByEmail_ReturnsEmptyOptional() {
        // Given no user is saved with this email

        // When
        Optional<User> userOptional = userRepository.getUserByEmail("nonexistent@gmail.com");

        // Then
        Assert.assertFalse(userOptional.isPresent());
    }

    @Test
    public void UserRepository_getAllByRole_ReturnsUsersWithRole() {
        // Given
        Role role = Role.USER;
        User user1 = User.builder()
                .email("user1@gmail.com")
                .password("user1pass")
                .firstname("User1")
                .lastname("Lastname1")
                .role(role)
                .build();
        User user2 = User.builder()
                .email("user2@gmail.com")
                .password("user2pass")
                .firstname("User2")
                .lastname("Lastname2")
                .role(role)
                .build();
        userRepository.saveAll(List.of(user1, user2));

        // When
        List<User> users = userRepository.getAllByRole(role);

        // Then
        assertEquals(2, users.size());
        assertEquals("user1@gmail.com", users.get(0).getEmail());
        assertEquals("user2@gmail.com", users.get(1).getEmail());
    }
}
