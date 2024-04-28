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
public class TextBlockCreateRequest {

    private boolean modified;

    @JsonProperty("raw_text")
    private String rawText;

    @JsonProperty("meaningful_text")
    private String meaningfulText;

    @JsonProperty("md_text")
    private String mdText;

}
