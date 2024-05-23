package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.NoteEditRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.NoteStatusUpdateRequest;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
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
public class NoteStatusUpdateRequestTest {

    @Autowired
    private JacksonTester<NoteStatusUpdateRequest> jsonTester;

    private String json;

    @Before
    public void setup(){
        json = """
                {
                    "note_status":"STOPPED"
                }
                """;
    }

    @Test
    public void givenJson_whenDeserialize_thenCorrectNoteStatusUpdateRequest() throws Exception {

        NoteStatusUpdateRequest noteStatusUpdateRequest = jsonTester.parseObject(json);

        assertThat(noteStatusUpdateRequest.getNoteStatus()).isEqualTo(NoteStatus.STOPPED);
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsInvalidFormatException() {

        String invalidJson = """
                {
                    "note_status":"STOPPEDs"
                }
                """;
        assertThrows(InvalidFormatException.class, ()-> {
            NoteStatusUpdateRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsJsonParseException() {

        String invalidJson = """
                {
                    "note_status": STOPPED
                }
                """;
        assertThrows(JsonParseException.class, ()-> {
            NoteStatusUpdateRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }
}
