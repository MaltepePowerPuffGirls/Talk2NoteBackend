package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
import com.Talk2Note.Talk2NoteBackend.api.dto.NoteResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class MemberResponseTest {

    @Autowired
    private JacksonTester<MemberResponse> jsonTester;

    private MemberResponse memberResponse;

    @Before
    public void setup(){
        memberResponse = MemberResponse.builder()
                .id(1)
                .authority("owner")
                .accepted(true)
                .userId(1)
                .noteId(1)
                .invitedAt(new Date())
                .acceptedAt(new Date())
                .modifiedAt(new Date())
                .build();

    }

    @Test
    public void givenMemberResponse_whenSerialize_thenCorrectJson() throws Exception {
        JsonContent<MemberResponse> memberResponseJsonContent = jsonTester.write(memberResponse);

        assertThat(memberResponseJsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(memberResponse.getId());
        assertThat(memberResponseJsonContent).extractingJsonPathStringValue("$.authority").isEqualTo(memberResponse.getAuthority());
        assertThat(memberResponseJsonContent).extractingJsonPathBooleanValue("$.accepted").isEqualTo(true);
        assertThat(memberResponseJsonContent).extractingJsonPathNumberValue("$.user_id").isEqualTo(1);
        assertThat(memberResponseJsonContent).extractingJsonPathNumberValue("$.note_id").isEqualTo(1);
        assertThat(memberResponseJsonContent).extractingJsonPathStringValue("$.invited_at").isNotNull();
        assertThat(memberResponseJsonContent).extractingJsonPathStringValue("$.accepted_at").isNotNull();
        assertThat(memberResponseJsonContent).extractingJsonPathStringValue("$.modified_a-t").isNotNull();

    }
}
