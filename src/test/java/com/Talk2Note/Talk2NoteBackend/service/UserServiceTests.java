package com.Talk2Note.Talk2NoteBackend.service;

import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.UserRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {

    }

    @Test
    public void UserManager_saveUser_ReturnsSuccessResult() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setAbout("Test user");

        Result result = userService.saveUser(user);

        assertTrue(result.isSuccess());
        assertEquals("User saved", result.getMessage());
    }

    @Test
    public void UserManager_getUserById_ReturnsUserData() {

        int userId = 1;

        DataResult<User> result = userService.getUserById(userId);

        assertTrue(result.isSuccess());
        assertEquals("User found!", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(userId, result.getData().getId());
    }

}
