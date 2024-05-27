package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.core.enums.TokenType;
import com.Talk2Note.Talk2NoteBackend.entity.Token;
import com.Talk2Note.Talk2NoteBackend.entity.User;
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

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class TokenRepositoryTests {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Token token1, token2;

    @Before
    public void setup() {

        user = User.builder().email("user1@gmail.com").build();

        token1 = Token.builder()
                        .token("token_1")
                        .tokenType(TokenType.BEARER)
                        .user(user)
                        .build();

        token2 = Token.builder()
                        .token("token_2")
                        .tokenType(TokenType.BEARER)
                        .user(user)
                        .build();

        userRepository.save(user);

        tokenRepository.save(token1);
        tokenRepository.save(token2);
    }

    @Test
    public void TokenRepository_findAllValidTokenByUser_ReturnsValidTokens() {

        List<Token> validTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        assertEquals(2, validTokens.size());

        List<String> tokens = List.of("token_1", "token_2");
        assertTrue(tokens.containsAll(validTokens.stream()
                .map(Token::getToken)
                .toList()));
    }

    @Test
    public void TokenRepository_findByToken_ReturnsTokenIfExists() {

        Optional<Token> optionalToken = tokenRepository.findByToken(token1.getToken());

        assertTrue(optionalToken.isPresent());
        Token foundToken = optionalToken.get();
        assertEquals(token1.getToken(), foundToken.getToken());
    }

    @Test
    public void TokenRepository_findByToken_ReturnsEmptyOptionalIfNotExists() {

        String nonExistentTokenString = "nonExistentToken";

        Optional<Token> optionalToken = tokenRepository.findByToken(nonExistentTokenString);

        assertFalse(optionalToken.isPresent());
    }

}
