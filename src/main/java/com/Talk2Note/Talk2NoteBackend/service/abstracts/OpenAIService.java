package com.Talk2Note.Talk2NoteBackend.service.abstracts;


import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Note;

import java.util.Map;

public interface OpenAIService {

    Result rawToMeaningful(Note note);
    Result MdAuto(Note note);
}
