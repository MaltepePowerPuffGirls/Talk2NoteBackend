package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberCreateRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockCreateRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockEditRequest;
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
public class TextBlockEditRequestTest {

    @Autowired
    private JacksonTester<TextBlockEditRequest> jsonTester;

    private String json;

    @Before
    public void setup(){
        json = """
                {
                    "modified": "true",
                    "raw_text": "raw text",
                    "meaningful_text": "meaningful text",
                    "md_text": "md text"
                }
                """;
    }

    @Test
    public void givenJson_whenDeserialize_thenCorrectTextBlockEditRequest() throws Exception {

        TextBlockEditRequest textBlockEditRequest = jsonTester.parseObject(json);

        assertThat(textBlockEditRequest.isModified()).isEqualTo(true);
        assertThat(textBlockEditRequest.getRawText()).isEqualTo("raw text");
        assertThat(textBlockEditRequest.getMeaningfulText()).isEqualTo("meaningful text");
        assertThat(textBlockEditRequest.getMdText()).isEqualTo("md text");
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsInvalidFormatException() {

        String invalidJson = """
                {
                    "modified": "truee",
                    "raw_text": "raw text",
                    "meaningful_text": "meaningful text",
                    "md_text": "md text"
                }
                """;
        assertThrows(InvalidFormatException.class, ()-> {
            TextBlockEditRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }

    @Test
    public void givenInvalidJson_whenDeserialize_throwsJsonParseException() {

        String invalidJson = """
                {
                    "modified": "true"1,
                    "raw_text": "raw text",
                    "meaningful_text": "meaningful text",
                    "md_text": "md text"
                }
                """;
        assertThrows(JsonParseException.class, ()-> {
            TextBlockEditRequest invalidRequest = jsonTester.parseObject(invalidJson);

        });
    }
}
