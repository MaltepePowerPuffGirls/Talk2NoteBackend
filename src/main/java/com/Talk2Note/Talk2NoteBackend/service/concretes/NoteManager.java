package com.Talk2Note.Talk2NoteBackend.service.concretes;

import com.Talk2Note.Talk2NoteBackend.api.dto.*;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
import com.Talk2Note.Talk2NoteBackend.core.results.*;
import com.Talk2Note.Talk2NoteBackend.core.utils.AuthUserUtil;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.NoteRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.NoteService;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.TextBlockService;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.Talk2Note.Talk2NoteBackend.core.utils.DtoMapUtil.*;

@Service
@RequiredArgsConstructor
public class NoteManager implements NoteService {
    private final NoteRepository noteRepository;
    private final TextBlockService textBlockService;
    private final AuthUserUtil authUserUtil;

    @Override
    public Result createNote(NoteCreateRequest request) {
        User authUser = authUserUtil.getAuthenticatedUser();
        if(authUser == null){
            return new ErrorResult("User not authenticated!");
        }

        Note note = Note.builder()
                .noteName(request.getNoteName())
                .priority(request.getPriority())
                .noteStatus(NoteStatus.STARTED)
                .pinned(false)
                .description(request.getDescription())
                .author(authUser)
                .textBlocks(new ArrayList<>())
                .members(new ArrayList<>())
                .build();
        return saveNote(note);
    }

    @Override
    public Result addTextBlock(int noteId, TextBlockCreateRequest request) {
        DataResult<Note> noteResult = getNote(noteId);
        if(!noteResult.isSuccess()){
            return new ErrorDataResult<>(noteResult.getMessage());
        }
        Note note = (Note) noteResult.getData();
        List<TextBlock> blocks = note.getTextBlocks();

        blocks.sort(Comparator.comparingInt(TextBlock::getRowNumber).reversed());
        int lastRowNumber = 0;
        if (!blocks.isEmpty()) {
            lastRowNumber = blocks.get(0).getRowNumber();
        }

        TextBlock textBlock = TextBlock.builder()
                .rowNumber(lastRowNumber + 1)
                .modified(false)
                .rawText(request.getRawText())
                .meaningfulText(request.getMeaningfulText())
                .mdText(request.getMdText())
                .note(note)
                .build();
        return saveNote(note);
    }

    @Override
    public DataResult<NoteResponse> getNoteById(int noteId) {
        DataResult<Note> noteResult = getNote(noteId);
        if(!noteResult.isSuccess()){
            return new ErrorDataResult<>(noteResult.getMessage());
        }
        Note note = (Note) noteResult.getData();
        NoteResponse response = generateNoteResponse(note);

        return new SuccessDataResult<>(response, "Note fetched");
    }

    @Override
    public DataResult<List<NoteResponse>> getAllNotes() {
        List<Note> notes = noteRepository.findAll();

        List<NoteResponse> noteResponses = generateNoteResponses(notes);

        return new SuccessDataResult<>(noteResponses, "All notes fetched!");
    }

    @Override
    public DataResult<List<Note>> getAllNotesByUser(User user) {
        List<Note> notes = noteRepository.findAllNotesByUser(user);
        return new SuccessDataResult<>(notes, "All notes fetched for specified user!");
    }

    @Override
    public DataResult<List<Note>> getAllNotesByAuthUser() {
        User authUser = authUserUtil.getAuthenticatedUser();
        if (authUser == null){
            return new ErrorDataResult<>("User not authorized!");
        }
        return getAllNotesByUser(authUser);
    }

    @Override
    public DataResult<List<TextBlockResponse>> getNoteBlocks(int noteId) {
        DataResult<Note> noteResult = getNote(noteId);
        if(!noteResult.isSuccess()){
            return new ErrorDataResult<>(noteResult.getMessage());
        }
        Note note = (Note) noteResult.getData();

        List<TextBlockResponse> response = generateTextBlockResponses(note.getTextBlocks());
        return new SuccessDataResult<>(response, "All textblocks fetched for note: "+ noteId);
    }

    @Override
    public DataResult<List<MemberResponse>> getNoteMembers(int noteId) {
        DataResult<Note> noteResult = getNote(noteId);
        if(!noteResult.isSuccess()){
            return new ErrorDataResult<>(noteResult.getMessage());
        }
        Note note = (Note) noteResult.getData();

        List<MemberResponse> response = generateMemberResponses(note.getMembers());
        return new SuccessDataResult<>(response, "All members fetched for note: "+ noteId);
    }

    @Override
    public Result editNote(int noteId, NoteEditRequest request) {
        DataResult<Note> noteResult = getNote(noteId);
        if (!noteResult.isSuccess()){
            return new ErrorResult(noteResult.getMessage());
        }

        Note note = (Note) noteResult.getData();

        note.setNoteName(request.getNoteName());
        note.setPriority(request.getPriority());
        note.setNoteStatus(request.getNoteStatus());
        note.setPinned(request.isPinned());
        note.setDescription(request.getDescription());

        return saveNote(note);
    }

    @Override
    public Result deleteNoteById(int noteId) {
        User authUser = authUserUtil.getAuthenticatedUser();
        if(authUser == null){
            return new ErrorResult("User not authenticated!");
        }

        DataResult<Note> noteResult = getNote(noteId);
        if (!noteResult.isSuccess()){
            return new ErrorResult(noteResult.getMessage());
        }
        Note note = (Note) noteResult.getData();

        if(note.getAuthor() != authUser){
            return new ErrorResult("User not authorized for note deletion!");
        }
        return deleteNote(note);
    }

    private Result saveNote(Note note) {
        try {
            noteRepository.save(note);
        }catch (Exception e){
            return new ErrorResult("Unexpected Error Occurred: " + e.getMessage());
        }
        return new SuccessResult("Note saved!");
    }

    private Result deleteNote(Note note){
        try {
            noteRepository.delete(note);
        }catch (Exception e){
            return new ErrorResult("Unexpected Error Occurred: " + e.getMessage());
        }
        return new SuccessResult("Note deleted!");
    }

    private DataResult<Note> getNote(int noteId){
        Note note = noteRepository.findById(noteId).orElse(null);
        if(note == null){
            return new ErrorDataResult<>("Note not found by id: " + noteId);
        }
        return new SuccessDataResult<>(note, "Note found!");
    }
}
