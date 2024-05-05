package com.Talk2Note.Talk2NoteBackend.service.abstracts;

import com.Talk2Note.Talk2NoteBackend.core.enums.OpenAIRoleType;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Note;

import java.util.Map;

public interface OpenAIService {

    Result rawToMeaningful(Note note);
    Result MdAuto(Note note);

    String generatePrompt(Map<Integer, String> content);
    Map<Integer, String> convertGeneratedResponse(String responseText);
    String chatByRole(Map<Integer, String> chat, OpenAIRoleType type);
    Map<Integer, String> chatByRoleParsed(Map<Integer, String> chat, OpenAIRoleType type);

}
