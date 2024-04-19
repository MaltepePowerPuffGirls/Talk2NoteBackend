package com.Talk2Note.Talk2NoteBackend.service.concretes;

import com.Talk2Note.Talk2NoteBackend.api.dto.UserDto;
import com.Talk2Note.Talk2NoteBackend.api.dto.UserEditRequest;
import com.Talk2Note.Talk2NoteBackend.core.results.*;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.UserRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final UserRepository userRepository;

    @Override
    public DataResult<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDto> userDtos = users.stream()
                .map(user -> UserDto.builder()
                        .email(user.getEmail())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .about(user.getAbout())
                        .role(user.getRole().name())
                        .build())
                .collect(Collectors.toList());

        return new SuccessDataResult<>(userDtos, "All user fetched");
    }

    @Override
    public Result saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new ErrorResult("Unexpected Error Occurred: " + e.getMessage());
        }
        return new SuccessResult("User saved");
    }

    @Override
    public DataResult getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email).orElse(null);
        if (user == null) {
            return new ErrorDataResult("User not found by email: " + email);
        }
        return new SuccessDataResult(user, "User found");
    }

    @Override
    public Result modifyUser(UserEditRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DataResult userResult = getUserByEmail(authentication.getName());
        if (!userResult.isSuccess()) {
            return new ErrorResult(userResult.getMessage());
        }

        User user = (User) userResult.getData();

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setAbout(request.getAbout());

        Result result = saveUser(user);
        if (!result.isSuccess()) {
            return new ErrorResult(result.getMessage());
        }

        return new SuccessResult("User modified");
    }

}
