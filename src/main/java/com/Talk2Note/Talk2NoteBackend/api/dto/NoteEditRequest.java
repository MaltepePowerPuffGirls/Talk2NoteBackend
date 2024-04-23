package com.Talk2Note.Talk2NoteBackend.api.dto;

import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
import com.Talk2Note.Talk2NoteBackend.core.enums.Priority;
import com.Talk2Note.Talk2NoteBackend.entity.Member;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteEditRequest {

    @JsonProperty("note_name")
    private String noteName;

    private Priority priority;

    @JsonProperty("note_status")
    private NoteStatus noteStatus;

    private boolean pinned;

    private String description;

}
