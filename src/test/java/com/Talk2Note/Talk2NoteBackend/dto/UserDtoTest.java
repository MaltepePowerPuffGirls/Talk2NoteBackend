package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.UserDto;
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
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jsonTester;

    private String email = "mehmet.deloglu@gmail.com";
    private String firstname = "Mehmet";
    private String lastname = "Deloğlu";
    private String about = "Software Engineer";
    private String role = "user";

    private String validJson;

    @Before
    public void setup() {
        validJson = String.format("{\"email\": \"%s\", \"firstname\": \"%s\", \"lastname\": \"%s\", \"about\": \"%s\", \"role\": \"%s\"}",
                email, firstname, lastname, about, role);
    }

    @Test
    public void givenValidJson_whenDeserialize_thenCorrectUserDto() throws Exception {
        UserDto userDto = jsonTester.parseObject(validJson);

        assertThat(userDto.getEmail()).isEqualTo(email);
        assertThat(userDto.getFirstname()).isEqualTo(firstname);
        assertThat(userDto.getLastname()).isEqualTo(lastname);
        assertThat(userDto.getAbout()).isEqualTo(about);
        assertThat(userDto.getRole()).isEqualTo(role);
    }

    @Test
    public void givenUserDto_whenSerialize_thenCorrectJson() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        userDto.setFirstname(firstname);
        userDto.setLastname(lastname);
        userDto.setAbout(about);
        userDto.setRole(role);

        JsonContent<UserDto> userDtoJson = jsonTester.write(userDto);

        assertThat(userDtoJson).extractingJsonPathStringValue("$.email").isEqualTo(email);
        assertThat(userDtoJson).extractingJsonPathStringValue("$.firstname").isEqualTo(firstname);
        assertThat(userDtoJson).extractingJsonPathStringValue("$.lastname").isEqualTo(lastname);
        assertThat(userDtoJson).extractingJsonPathStringValue("$.about").isEqualTo(about);
        assertThat(userDtoJson).extractingJsonPathStringValue("$.role").isEqualTo(role);
    }

    @Test
    public void givenInvalidJson_whenDeserialize_thenHandleMissingFields() throws Exception {
        String invalidJson = "{\"email\": \"mehmet.deloglu@gmail.com\", \"firstname\": \"Mehmet\", \"lastname\": \"Deloğlu\", \"about\": \"Software Engineer\"}"; // eksik role

        UserDto userDto = jsonTester.parseObject(invalidJson);

        assertThat(userDto.getEmail()).isEqualTo(email);
        assertThat(userDto.getFirstname()).isEqualTo(firstname);
        assertThat(userDto.getLastname()).isEqualTo(lastname);
        assertThat(userDto.getAbout()).isEqualTo(about);
        assertThat(userDto.getRole()).isNull();
    }

    @Test
    public void givenPartiallyValidJson_whenDeserialize_thenHandleMissingFields() throws Exception {
        String partiallyValidJson = "{\"email\": \"mehmet.deloglu@gmail.com\", \"firstname\": \"Mehmet\", \"lastname\": \"Deloğlu\"}"; // eksik about ve role

        UserDto userDto = jsonTester.parseObject(partiallyValidJson);

        assertThat(userDto.getEmail()).isEqualTo(email);
        assertThat(userDto.getFirstname()).isEqualTo(firstname);
        assertThat(userDto.getLastname()).isEqualTo(lastname);
        assertThat(userDto.getAbout()).isNull();
        assertThat(userDto.getRole()).isNull();
    }
}
