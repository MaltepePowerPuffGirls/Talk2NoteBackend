package com.Talk2Note.Talk2NoteBackend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEditRequest {

    private int id;

    private String authority;

    private boolean accepted;

    @JsonProperty("note_id")
    private int noteId;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("invited_at")
    private Date invitedAt;

    @JsonProperty("accepted_at")
    private Date acceptedAt;

    @JsonProperty("modified_at")
    private Date modifiedAt;
}
