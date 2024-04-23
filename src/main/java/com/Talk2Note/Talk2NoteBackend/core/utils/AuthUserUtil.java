package com.Talk2Note.Talk2NoteBackend.core.utils;

import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUserUtil {

    private final UserService userService;

    @Autowired
    public AuthUserUtil(UserService userService) {
        this.userService = userService;
    }

    public User getAuthenticatedUser() {

        Authentication auth = this.getAuthentication();
        if (auth == null) { return null; }

        DataResult result = userService.getUserByEmail(auth.getName());
        return (User) result.getData();

    }

    public boolean IsRequestAuthenticated() {
        Authentication auth = this.getAuthentication();
        return auth.isAuthenticated();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
