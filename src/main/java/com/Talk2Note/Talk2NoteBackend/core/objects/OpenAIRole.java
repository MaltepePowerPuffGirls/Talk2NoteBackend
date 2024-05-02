package com.Talk2Note.Talk2NoteBackend.core.objects;

import com.Talk2Note.Talk2NoteBackend.core.enums.OpenAIRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAIRole {

    private String actorName;
    private String description;
    private OpenAIRoleType roleType;
    private float temperature;

    public String introduce() {
        switch (this.roleType) {
            case RAW_TO_MEANINGFUL:
                return "I'm " + this.actorName + ", specializing in transforming raw text into meaningful sentences. " + this.description;
            case MD_AUTO:
                return "I'm " + this.actorName + ", adept at generating Markdown-formatted text automatically. " + this.description;
            default:
                return "I'm " + this.actorName + ", ready to assist in various roles. " + this.description;
        }
    }

}
