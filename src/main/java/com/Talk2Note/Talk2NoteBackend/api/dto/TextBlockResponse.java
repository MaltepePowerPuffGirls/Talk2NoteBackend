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
public class TextBlockResponse {

    private int id;

    @JsonProperty("row_number")
    private int rowNumber;

    private boolean modified;

    @JsonProperty("raw_text")
    private String rawText;

    @JsonProperty("meaningful_text")
    private String meaningfulText;

    @JsonProperty("md_text")
    private String mdText;

    @JsonProperty("note_id")
    private int noteId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("modified_at")
    private Date modifiedAt;
}
