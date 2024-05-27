package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberEditRequest;
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
public class MemberEditRequestTest {

    @Autowired
    private JacksonTester<MemberEditRequest> jsonTester;

    private String json;

    @Before
    public void setup(){
        json = """
                {
                    "id": 1,
                    "authority": "owner",
                    "accepted": "true",
                    "note_id": 1,
                    "user_id": 1
                }
                """;
    }

    @Test
    public void givenJson_whenDeserialize_thenCorrectMemberEditRequest() throws Exception {

        MemberEditRequest memberEditRequest = jsonTester.parseObject(json);

        assertThat(memberEditRequest.getNoteId()).isEqualTo(1);
        assertThat(memberEditRequest.getAuthority()).isEqualTo("owner");
        assertThat(memberEditRequest.isAccepted()).isEqualTo(true);
        assertThat(memberEditRequest.getNoteId()).isEqualTo(1);
        assertThat(memberEditRequest.getUserId()).isEqualTo(1);
        assertThat(memberEditRequest.getInvitedAt()).isNull();
        assertThat(memberEditRequest.getAcceptedAt()).isNull();
        assertThat(memberEditRequest.getModifiedAt()).isNull();
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsInvalidFormatException() {

        String invalidJson = """
                {
                    "id": 1,
                    "authority": "owner",
                    "accepted": "truee",
                    "note_id": "not an id",
                    "user_id": 1
                }
                """;
        assertThrows(InvalidFormatException.class, ()-> {
            MemberEditRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsJsonParseException() {

        String invalidJson = """
                {
                    "id": 1,
                    "authority": "owner",
                    "accepted": "true",
                    "note_id": ,
                    "user_id": 1
                }
                """;
        assertThrows(JsonParseException.class, ()-> {
            MemberEditRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }
}
