package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.NoteEditRequest;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
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
public class NoteEditRequestTest {

    @Autowired
    private JacksonTester<NoteEditRequest> jsonTester;

    private String json;

    @Before
    public void setup(){
        json =  """
            {
                "note_title": "New Note",
                "priority": "MEDIUM",
                "note_status": "RECORDING",
                "pinned": "true",
                "description": "This is a new note"
            }
            """;
    }

    @Test
    public void givenJson_whenDeserialize_thenCorrectNoteEditRequest() throws Exception {
        NoteEditRequest noteEditRequest = jsonTester.parseObject(json);

        assertThat(noteEditRequest.getNoteTitle()).isEqualTo("New Note");
        assertThat(noteEditRequest.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(noteEditRequest.getNoteStatus()).isEqualTo(NoteStatus.RECORDING);
        assertThat(noteEditRequest.isPinned()).isEqualTo(true);
        assertThat(noteEditRequest.getDescription()).isEqualTo("This is a new note");
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsInvalidFormatException() {

        String invalidJson = """
            {
                "note_title": "New Note",
                "priority": "MEDIUMs",
                "note_status": "RECORDINsG",
                "pinned": "truee",
                "description": "This is a new note"
            }
            """;
        assertThrows(InvalidFormatException.class, ()-> {
            NoteEditRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsJsonParseException() {

        String invalidJson = """
            {
                "note_title": "New Note",
                "priority": MEDIUM,
                "note_status": "RECORDING",
                "pinned": "true",
                "description": "This is a new note"
            }
            """;
        assertThrows(JsonParseException.class, ()-> {
            NoteEditRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }
}
