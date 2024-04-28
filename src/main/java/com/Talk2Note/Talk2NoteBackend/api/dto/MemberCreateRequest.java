package com.Talk2Note.Talk2NoteBackend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateRequest {

    private String authority;

    @JsonProperty("note_id")
    private int noteId;

    @JsonProperty("user_id")
    private int userId;

}
