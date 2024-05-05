package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(
        connection = EmbeddedDatabaseConnection.H2,
        replace = AutoConfigureTestDatabase.Replace.NONE)
public class NoteRepositoryTests {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1, user2;
    private List<Note> notes;

    @Before
    public void setup() {
        user1 = User.builder().email("user1@gmail.com").build();
        user2 = User.builder().email("user2@gmail.com").build();

        notes = new ArrayList<>();
        notes.add(Note.builder().noteTitle("Note 1").author(user1).build());
        notes.add(Note.builder().noteTitle("Note 2").author(user1).build());
        notes.add(Note.builder().noteTitle("Note 3").author(user2).build());

        userRepository.save(user1);
        userRepository.save(user2);

        noteRepository.saveAll(notes);
    }

    @Test
    public void testFindAllNotesByUser_WithValidUser_ReturnsNotes() {

        List<Note> actualNotes = noteRepository.findAllNotesByUser(user1);

        assertEquals(2, actualNotes.size());
        for (Note actualNote : actualNotes) {
            assertEquals(user1.getEmail(), actualNote.getAuthor().getEmail());
        }
    }
}