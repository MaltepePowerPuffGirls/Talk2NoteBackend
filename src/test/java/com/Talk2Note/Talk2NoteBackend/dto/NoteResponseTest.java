package com.Talk2Note.Talk2NoteBackend.dto;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
import com.Talk2Note.Talk2NoteBackend.api.dto.NoteResponse;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteType;
import com.Talk2Note.Talk2NoteBackend.core.enums.Priority;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class NoteResponseTest {

    @Autowired
    private JacksonTester<NoteResponse> jsonTester;

    private NoteResponse noteResponse;

    @Before
    public void setup() {
        noteResponse = NoteResponse.builder()
                .id(1)
                .noteTitle("Test Note")
                .priority(Priority.HIGH)
                .noteStatus(NoteStatus.RECORDING)
                .noteType(NoteType.DEVELOPER)
                .pinned(true)
                .description("This is a test note")
                .authorId(100)
                .textBlocks(List.of(new TextBlockResponse()))
                .members(List.of(new MemberResponse()))
                .createdAt(new Date())
                .modifiedAt(new Date())
                .build();
    }

    @Test
    public void givenNoteResponse_whenSerialize_thenCorrectJson() throws Exception {
        JsonContent<NoteResponse> noteResponseJson = jsonTester.write(noteResponse);

        assertThat(noteResponseJson).extractingJsonPathNumberValue("$.id").isEqualTo(noteResponse.getId());
        assertThat(noteResponseJson).extractingJsonPathStringValue("$.note_title").isEqualTo(noteResponse.getNoteTitle());
        assertThat(noteResponseJson).extractingJsonPathStringValue("$.priority").isEqualTo(noteResponse.getPriority().name());
        assertThat(noteResponseJson).extractingJsonPathStringValue("$.note_status").isEqualTo(noteResponse.getNoteStatus().name());
        assertThat(noteResponseJson).extractingJsonPathStringValue("$.note_type").isEqualTo(noteResponse.getNoteType().name());
        assertThat(noteResponseJson).extractingJsonPathBooleanValue("$.pinned").isEqualTo(noteResponse.isPinned());
        assertThat(noteResponseJson).extractingJsonPathStringValue("$.description").isEqualTo(noteResponse.getDescription());
        assertThat(noteResponseJson).extractingJsonPathNumberValue("$.author_id").isEqualTo(noteResponse.getAuthorId());
        assertThat(noteResponseJson).extractingJsonPathStringValue("$.created_at").isNotNull();
        assertThat(noteResponseJson).extractingJsonPathStringValue("$.modified_at").isNotNull();
    }
}
