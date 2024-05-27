package com.Talk2Note.Talk2NoteBackend.service.abstracts;

import com.Talk2Note.Talk2NoteBackend.api.dto.*;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.User;

import java.util.List;

public interface NoteService {

    Result createNote(NoteCreateRequest request);

    Result addTextBlock(int noteId, TextBlockCreateRequest request);

    DataResult<NoteResponse> getNoteResponseById(int noteId);

    DataResult<List<NoteResponse>> getAllNotes();

    DataResult<List<NoteResponse>> getAllNotesByUser(User user);

    DataResult<List<NoteResponse>> getAllNotesByAuthUser();

    DataResult<List<TextBlockResponse>> getNoteBlocks(int noteId);

    DataResult<List<MemberResponse>> getNoteMembers(int noteId);

    Result editNote(int noteId, NoteEditRequest request);

    Result updateNoteStatus(int noteId, NoteStatusUpdateRequest request);

    Result deleteNoteById(int noteId);
    void updateNote(Note note);

}
