package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.UserEditRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class UserEditRequestTest {

    @Autowired
    private JacksonTester<UserEditRequest> jsonTester;

    private String firstname = "Mehmet";
    private String lastname = "Deloğlu";
    private String about = "Software Engineer";

    private String validJson;

    @Before
    public void setup() {
        validJson = String.format("{\"firstname\": \"%s\", \"lastname\": \"%s\", \"about\": \"%s\"}",
                firstname, lastname, about);
    }

    @Test
    public void givenValidJson_whenDeserialize_thenCorrectUserEditRequest() throws Exception {
        UserEditRequest userEditRequest = jsonTester.parseObject(validJson);

        assertThat(userEditRequest.getFirstname()).isEqualTo(firstname);
        assertThat(userEditRequest.getLastname()).isEqualTo(lastname);
        assertThat(userEditRequest.getAbout()).isEqualTo(about);
    }

    @Test
    public void givenUserEditRequest_whenSerialize_thenCorrectJson() throws Exception {
        UserEditRequest userEditRequest = UserEditRequest.builder()
                .firstname(firstname)
                .lastname(lastname)
                .about(about)
                .build();

        JsonContent<UserEditRequest> userEditRequestJson = jsonTester.write(userEditRequest);

        assertThat(userEditRequestJson).extractingJsonPathStringValue("$.firstname").isEqualTo(firstname);
        assertThat(userEditRequestJson).extractingJsonPathStringValue("$.lastname").isEqualTo(lastname);
        assertThat(userEditRequestJson).extractingJsonPathStringValue("$.about").isEqualTo(about);
    }

    @Test
    public void givenInvalidJson_whenDeserialize_thenHandleMissingFields() throws Exception {
        String invalidJson = "{\"firstname\": \"Mehmet\", \"lastname\": \"Deloğlu\"}";

        UserEditRequest userEditRequest = jsonTester.parseObject(invalidJson);

        assertThat(userEditRequest.getFirstname()).isEqualTo(firstname);
        assertThat(userEditRequest.getLastname()).isEqualTo(lastname);
        assertThat(userEditRequest.getAbout()).isNull();
    }

    @Test
    public void givenPartiallyValidJson_whenDeserialize_thenHandleMissingFields() throws Exception {
        String partiallyValidJson = "{\"firstname\": \"Mehmet\"}";

        UserEditRequest userEditRequest = jsonTester.parseObject(partiallyValidJson);

        assertThat(userEditRequest.getFirstname()).isEqualTo(firstname);
        assertThat(userEditRequest.getLastname()).isNull();
        assertThat(userEditRequest.getAbout()).isNull();
    }
}
