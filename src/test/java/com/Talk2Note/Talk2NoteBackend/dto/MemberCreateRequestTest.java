package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberCreateRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockCreateRequest;
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
public class MemberCreateRequestTest {

    @Autowired
    private JacksonTester<MemberCreateRequest> jsonTester;

    private String json;

    @Before
    public void setup(){
        json = """
                {
                    "authority": "owner",
                    "note_id": 1,
                    "user_id": 1
                }
                """;
    }

    @Test
    public void givenJson_whenDeserialize_thenCorrectMemberCreateRequest() throws Exception {

        MemberCreateRequest memberCreateRequest = jsonTester.parseObject(json);

        assertThat(memberCreateRequest.getAuthority()).isEqualTo("owner");
        assertThat(memberCreateRequest.getNoteId()).isEqualTo(1);
        assertThat(memberCreateRequest.getUserId()).isEqualTo(1);
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsInvalidFormatException() {

        String invalidJson = """
                {
                    "authority": "owner",
                    "note_id": "not an id",
                    "user_id": "1"
                }
                """;
        assertThrows(InvalidFormatException.class, ()-> {
            MemberCreateRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsJsonParseException() {

        String invalidJson = """
                {
                    "authority": "owner",
                    "note_id": ,
                    "user_id": "1"
                }
                """;
        assertThrows(JsonParseException.class, ()-> {
            MemberCreateRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }
}
