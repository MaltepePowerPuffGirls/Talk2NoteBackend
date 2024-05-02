package com.Talk2Note.Talk2NoteBackend.service.abstracts;

import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Note;

public interface OpenAIService {

    Result rawToMeaningful(Note note);
    Result MdAuto(Note note);

}
