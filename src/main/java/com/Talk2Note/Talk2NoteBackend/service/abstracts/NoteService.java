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

    DataResult<NoteResponse> getNoteById(int noteId);
    DataResult<List<NoteResponse>> getAllNotes();

    DataResult<List<Note>> getAllNotesByUser(User user);

    DataResult<List<Note>> getAllNotesByAuthUser();

    DataResult<List<TextBlockResponse>> getNoteBlocks(int noteId);

    DataResult<List<MemberResponse>> getNoteMembers(int noteId);

    Result editNote(int noteId, NoteEditRequest request);

    Result deleteNoteById(int noteId);

    private Result saveNote(Note note) {return null;}
    
    private Result deleteNote(Note note){return  null;}

    private DataResult<Note> getNote(int noteId){return null;}
}
