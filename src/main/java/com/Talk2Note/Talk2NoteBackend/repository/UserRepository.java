package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.core.enums.Role;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> getUserById(int id);
    Optional<User> getUserByEmail(String email);

    List<User> getAllByRole(Role role);

}
