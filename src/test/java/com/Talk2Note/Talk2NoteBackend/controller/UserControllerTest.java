package com.Talk2Note.Talk2NoteBackend.controller;

import com.Talk2Note.Talk2NoteBackend.api.controller.UserController;
import com.Talk2Note.Talk2NoteBackend.api.dto.UserDto;
import com.Talk2Note.Talk2NoteBackend.api.dto.UserEditRequest;
import com.Talk2Note.Talk2NoteBackend.config.JwtAuthenticationFilter;
import com.Talk2Note.Talk2NoteBackend.config.JwtService;
import com.Talk2Note.Talk2NoteBackend.core.enums.Role;
import com.Talk2Note.Talk2NoteBackend.core.enums.TokenType;
import com.Talk2Note.Talk2NoteBackend.core.results.*;
import com.Talk2Note.Talk2NoteBackend.core.utils.AuthUserUtil;
import com.Talk2Note.Talk2NoteBackend.entity.Token;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.TokenRepository;
import com.Talk2Note.Talk2NoteBackend.repository.UserRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc()
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUserUtil authUserUtil;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private TokenRepository tokenRepository;


    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    private User authUser;
    private String jwtToken;
    private String baseUri = "/api/v1/user";

    @Before
    public void setup(){

        authUser = User.builder()
                .email("user1@gmail.com")
                .password("test_hashed_password")
                .role(Role.USER)
                .build();

        // Mock token generation and validation
        Mockito.when(jwtService.generateToken(authUser)).thenReturn("mocked-token");
        Mockito.when(jwtService.isTokenValid("mocked-token", authUser)).thenReturn(true);
        Mockito.when(authUserUtil.getAuthenticatedUser()).thenReturn(authUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authUser.getEmail(), authUser.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        jwtToken = "Bearer " + jwtService.generateToken(authUser);

        Token token =  Token.builder()
                .user(authUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(token);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(authUser);

    }

    @Test
    public void UserController_getAllUsers_ReturnsAllUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .firstname(authUser.getFirstname())
                .lastname(authUser.getLastname())
                .about(authUser.getAbout())
                .email(authUser.getEmail())
                .build();

        DataResult<List<UserDto>> response = new SuccessDataResult<>(
                List.of(userDto), "All user fetched");

        Mockito.when(userService.getAllUsers()).thenReturn(response);

        mockMvc.perform(get(baseUri)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("All user fetched"),
                jsonPath("$.data").exists(),
                jsonPath("$.data").isNotEmpty(),
                jsonPath("$.data").isArray()
        );
    }

    @Test
    public void UserController_modifyAuthUser_ReturnsSuccessResult() throws Exception {
        UserEditRequest userEditRequest = UserEditRequest.builder()
                .firstname(authUser.getFirstname())
                .lastname(authUser.getLastname())
                .about(authUser.getAbout())
                .build();

        Result result = new SuccessResult("User modified");

        Mockito.when(userService.modifyUser(userEditRequest)).thenReturn(result);

        mockMvc.perform(post(baseUri + "/edit")).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("User modified")
        );

    }

    @Test
    public void UserController_getAllUsers_ReturnsBadRequest_WhenUserNotAuth() throws Exception {
        Mockito.doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(Mockito.any(), Mockito.any(), Mockito.any());

        mockMvc.perform(get(baseUri)).andExpect(status().isUnauthorized());
    }

    @Test
    public void UserController_modifyAuthUser_ReturnsErrorResult() throws Exception {
        UserEditRequest invalidRequest = UserEditRequest.builder().build();

        Result result = new ErrorResult("Invalid Request");

        Mockito.when(userService.modifyUser(invalidRequest)).thenReturn(result);

        mockMvc.perform(post(baseUri + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtToken)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpectAll(
                    status().isBadRequest(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    jsonPath("$.success").value(false),
                    jsonPath("$.message").value("Invalid Request")
        );
    }

}
