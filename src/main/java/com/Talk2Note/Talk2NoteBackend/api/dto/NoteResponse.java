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

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {

    private int id;

    @JsonProperty("note_name")
    private String noteName;

    private Priority priority;

    @JsonProperty("note_status")
    private NoteStatus noteStatus;

    private boolean pinned;

    private String description;

    @JsonProperty("author_id")
    private int authorId;

    @JsonProperty("text_blocks")
    private List<TextBlockResponse> textBlocks;

    private List<MemberResponse> members;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("modified_at")
    private Date modifiedAt;
}
