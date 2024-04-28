package com.Talk2Note.Talk2NoteBackend.api.dto;

import com.Talk2Note.Talk2NoteBackend.core.enums.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteCreateRequest {

    @JsonProperty("note_name")
    private String noteName;

    private Priority priority;

    private String description;
}
