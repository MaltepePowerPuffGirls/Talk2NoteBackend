package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query(value = """
            select n from Note n \s
            where n.author = :user\s
            """)
    List<Note> findAllNotesByUser(User user);
}
