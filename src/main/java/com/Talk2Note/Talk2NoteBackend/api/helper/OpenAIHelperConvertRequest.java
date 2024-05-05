package com.Talk2Note.Talk2NoteBackend.api.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAIHelperConvertRequest {

    private String message;

}
