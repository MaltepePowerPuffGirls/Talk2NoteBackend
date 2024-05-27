package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
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
public class TextBlockResponseTest {

    @Autowired
    private JacksonTester<TextBlockResponse> jsonTester;

    private TextBlockResponse textBlockResponse;

    @Before
    public void setup() {
        textBlockResponse = TextBlockResponse.builder()
                .id(1)
                .rowNumber(1)
                .modified(false)
                .rawText("raw text")
                .meaningfulText("meaningful text")
                .mdText("md text")
                .noteId(1)
                .createdAt(new Date())
                .modifiedAt(new Date())
                .build();
    }

    @Test
    public void givenTextBlockResponse_whenSerialize_thenCorrectJson() throws Exception {
        JsonContent<TextBlockResponse> textBlockResponseJson = jsonTester.write(textBlockResponse);

        assertThat(textBlockResponseJson).extractingJsonPathNumberValue("$.id").isEqualTo(textBlockResponse.getId());
        assertThat(textBlockResponseJson).extractingJsonPathNumberValue("$.row_number").isEqualTo(textBlockResponse.getRowNumber());
        assertThat(textBlockResponseJson).extractingJsonPathBooleanValue("$.modified").isEqualTo(textBlockResponse.isModified());
        assertThat(textBlockResponseJson).extractingJsonPathStringValue("$.raw_text").isEqualTo(textBlockResponse.getRawText());
        assertThat(textBlockResponseJson).extractingJsonPathStringValue("$.meaningful_text").isEqualTo(textBlockResponse.getMeaningfulText());
        assertThat(textBlockResponseJson).extractingJsonPathStringValue("$.md_text").isEqualTo(textBlockResponse.getMdText());
        assertThat(textBlockResponseJson).extractingJsonPathNumberValue("$.note_id").isEqualTo(textBlockResponse.getNoteId());
        assertThat(textBlockResponseJson).extractingJsonPathStringValue("$.created_at").isNotNull();
        assertThat(textBlockResponseJson).extractingJsonPathStringValue("$.modified_at").isNotNull();
    }
}
