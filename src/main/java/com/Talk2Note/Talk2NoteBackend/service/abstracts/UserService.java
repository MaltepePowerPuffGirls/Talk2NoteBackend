package com.Talk2Note.Talk2NoteBackend.service.abstracts;

import com.Talk2Note.Talk2NoteBackend.api.dto.UserDto;
import com.Talk2Note.Talk2NoteBackend.api.dto.UserEditRequest;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.User;

import java.util.List;

public interface UserService {

    DataResult<List<UserDto>> getAllUsers();
    Result saveUser(User user);
    DataResult<User> getUserByEmail(String email);
    Result modifyUser(UserEditRequest request);
    DataResult<User> getUserById(int id);
}
