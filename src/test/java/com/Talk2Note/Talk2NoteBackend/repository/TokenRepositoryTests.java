package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.core.enums.TokenType;
import com.Talk2Note.Talk2NoteBackend.entity.Token;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


// BROKEN : TokenRepository Autowired NullPointerException
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(
        connection = EmbeddedDatabaseConnection.H2,
        replace = AutoConfigureTestDatabase.Replace.NONE)
public class TokenRepositoryTests {

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    public void TokenRepository_findAllValidTokenByUser_ReturnsValidTokens() {
        // Given: A user ID and some valid tokens associated with that user
        int userId = 1; // Assuming user ID 1
        Token token1 = Token.builder()
                .token("token_1")
                .tokenType(TokenType.BEARER)
                .user(User.builder().id(userId).build())
                .build();
        Token token2 = Token.builder()
                .token("token_2")
                .tokenType(TokenType.BEARER)
                .user(User.builder().id(userId).build())
                .build();
        tokenRepository.saveAll(List.of(token1, token2));

        // When: Retrieving valid tokens for the given user ID
        List<Token> validTokens = tokenRepository.findAllValidTokenByUser(userId);

        // Then: Ensure the returned tokens are valid and associated with the user
        assertEquals(2, validTokens.size());

        List<String> tokens = List.of("token_1", "token_2");
        assertTrue(tokens.containsAll(validTokens.stream()
                .map(Token::getToken)
                .toList()));
    }

    @Test
    public void TokenRepository_findByToken_ReturnsTokenIfExists() {
        // Given: A token string that exists in the database
        String tokenString = "sampleTokenString"; // Assuming this token exists
        Token token = Token.builder()
                .token(tokenString)
                .tokenType(TokenType.BEARER)
                .build();
        tokenRepository.save(token);

        // When: Retrieving a token by its token string
        Optional<Token> optionalToken = tokenRepository.findByToken(tokenString);

        // Then: Ensure the token is found
        assertTrue(optionalToken.isPresent());
        Token foundToken = optionalToken.get();
        assertEquals(tokenString, foundToken.getToken());
    }

    @Test
    public void TokenRepository_findByToken_ReturnsEmptyOptionalIfNotExists() {
        // Given: A token string that does not exist in the database
        String nonExistentTokenString = "nonExistentToken"; // Assuming this token doesn't exist

        // When: Retrieving a token by a non-existent token string
        Optional<Token> optionalToken = tokenRepository.findByToken(nonExistentTokenString);

        // Then: Ensure the token is not found
        assertFalse(optionalToken.isPresent());
    }

}
