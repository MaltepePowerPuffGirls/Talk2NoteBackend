package com.Talk2Note.Talk2NoteBackend.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final HttpServletResponse response;

    public void setRefreshCookie(
            String refreshToken
    ) {

        ResponseCookie cookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .maxAge((int)refreshExpiration)
                .sameSite("None")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void setRefreshCookieNull() {

        ResponseCookie cookie = ResponseCookie.from("refresh-token", null)
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .sameSite("None")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    }

}