package com.Talk2Note.Talk2NoteBackend.dto;

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
public class TextBlockCreateRequestTest {

    @Autowired
    private JacksonTester<TextBlockCreateRequest> jsonTester;

    private String json;

    @Before
    public void setup(){
        json = """
                {
                    "modified": "false",
                    "raw_text": "raw text",
                    "meaningful_text": "meaningful text",
                    "md_text": "md text"
                }
                """;
    }

    @Test
    public void givenJson_whenDeserialize_thenCorrectTextBlockCreateRequest() throws Exception {

        TextBlockCreateRequest textBlockCreateRequest = jsonTester.parseObject(json);

        assertThat(textBlockCreateRequest.isModified()).isEqualTo(false);
        assertThat(textBlockCreateRequest.getRawText()).isEqualTo("raw text");
        assertThat(textBlockCreateRequest.getMdText()).isEqualTo("md text");
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsInvalidFormatException() {

        String invalidJson = """
                {
                    "modified": "notboolean",
                    "raw_text": "raw text",
                    "meaningful_text": "meaningful text",
                    "md_text": "md text"
                }
                """;
        assertThrows(InvalidFormatException.class, ()-> {
            TextBlockCreateRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsJsonParseException() {

        String invalidJson = """
                {
                    "modified": "true"",
                    "raw_text": "raw text",
                    "meaningful_text": "meaningful text",
                    "md_text": "md text"
                }
                """;
        assertThrows(JsonParseException.class, ()-> {
            TextBlockCreateRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }
}
