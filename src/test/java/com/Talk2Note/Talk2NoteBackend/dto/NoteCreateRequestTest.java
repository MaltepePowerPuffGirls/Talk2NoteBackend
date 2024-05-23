package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberEditRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.NoteCreateRequest;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteType;
import com.Talk2Note.Talk2NoteBackend.core.enums.Priority;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@JsonTest
public class NoteCreateRequestTest {

    @Autowired
    private JacksonTester<NoteCreateRequest> jsonTester;

    private String json;

    @Before
    public void setup() {
        json = """
            {
                "note_title": "New Note",
                "priority": "MEDIUM",
                "note_type": "DEVELOPER",
                "description": "This is a new note"
            }
            """;
    }

    @Test
    public void givenJson_whenDeserialize_thenCorrectNoteCreateRequest() throws Exception {

        NoteCreateRequest noteCreateRequest = jsonTester.parseObject(json);

        assertThat(noteCreateRequest.getNoteTitle()).isEqualTo("New Note");
        assertThat(noteCreateRequest.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(noteCreateRequest.getNoteType()).isEqualTo(NoteType.DEVELOPER);
        assertThat(noteCreateRequest.getDescription()).isEqualTo("This is a new note");
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsInvalidFormatException() {

        String invalidJson = """
            {
                "note_title": "New Note",
                "priority": "MEDIUMMM",
                "note_type": "DEVELOPER",
                "description": "This is a new note"
            }
            """;
        assertThrows(InvalidFormatException.class, ()-> {
            NoteCreateRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsJsonParseException() {

        String invalidJson = """
            {
                "note_title": "New Note",
                "priority": MEDIUM,
                "note_type": "DEVELOPER",
                "description": "This is a new note"
            }
            """;
        assertThrows(JsonParseException.class, ()-> {
            NoteCreateRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }
}
