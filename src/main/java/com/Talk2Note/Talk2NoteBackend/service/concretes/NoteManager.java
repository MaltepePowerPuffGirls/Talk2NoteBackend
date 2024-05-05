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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .noteTitle(request.getNoteTitle())
                .priority(request.getPriority())
                .noteStatus(NoteStatus.RECORDING)
                .noteType(request.getNoteType())
                .description(request.getDescription())
                .author(authUser)
                .build();
        return saveNote(note);
    }

    @Override
    public Result addTextBlock(int noteId, TextBlockCreateRequest request) {
        DataResult<Note> noteResult = getNoteById(noteId);
        if(!noteResult.isSuccess()){
            return new ErrorDataResult<>(noteResult.getMessage());
        }
        Note note = (Note) noteResult.getData();

        if(!note.getNoteStatus().equals(NoteStatus.RECORDING)){
            return new ErrorResult("Note status should be RECORDING, provided: "+
                    note.getNoteStatus().name());
        }

        List<TextBlock> blocks = note.getTextBlocks();

        blocks.sort(Comparator.comparingInt(TextBlock::getRowNumber).reversed());
        int lastRowNumber = 0;
        if (!blocks.isEmpty()) {
            lastRowNumber = blocks.get(0).getRowNumber();
        }

        TextBlock textBlock = TextBlock.builder()
                .rowNumber(lastRowNumber + 1)
                .rawText(request.getRawText())
                .meaningfulText(request.getMeaningfulText())
                .mdText(request.getMdText())
                .note(note)
                .build();

        Result result = textBlockService.save(textBlock);
        if (!result.isSuccess()){
            return new ErrorResult(result.getMessage());
        }
        return saveNote(note);
    }

    @Override
    public DataResult<NoteResponse> getNoteResponseById(int noteId) {
        DataResult<Note> noteResult = getNoteById(noteId);
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
    public DataResult<List<NoteResponse>> getAllNotesByUser(User user) {
        List<Note> notes = noteRepository.findAllNotesByUser(user);
        List<NoteResponse> response = generateNoteResponses(notes);
        return new SuccessDataResult<>(response, "All notes fetched for specified user!");
    }

    @Override
    public DataResult<List<NoteResponse>> getAllNotesByAuthUser() {
        User authUser = authUserUtil.getAuthenticatedUser();
        if (authUser == null){
            return new ErrorDataResult<>("User not authorized!");
        }
        return getAllNotesByUser(authUser);
    }

    @Override
    public DataResult<List<TextBlockResponse>> getNoteBlocks(int noteId) {
        DataResult<Note> noteResult = getNoteById(noteId);
        if(!noteResult.isSuccess()){
            return new ErrorDataResult<>(noteResult.getMessage());
        }
        Note note = (Note) noteResult.getData();

        List<TextBlockResponse> response = generateTextBlockResponses(note.getTextBlocks());
        return new SuccessDataResult<>(response, "All textblocks fetched for note: "+ noteId);
    }

    @Override
    public DataResult<List<MemberResponse>> getNoteMembers(int noteId) {
        DataResult<Note> noteResult = getNoteById(noteId);
        if(!noteResult.isSuccess()){
            return new ErrorDataResult<>(noteResult.getMessage());
        }
        Note note = (Note) noteResult.getData();

        List<MemberResponse> response = generateMemberResponses(note.getMembers());
        return new SuccessDataResult<>(response, "All members fetched for note: "+ noteId);
    }

    @Override
    public Result editNote(int noteId, NoteEditRequest request) {
        DataResult<Note> noteResult = getNoteById(noteId);
        if (!noteResult.isSuccess()){
            return new ErrorResult(noteResult.getMessage());
        }

        Note note = (Note) noteResult.getData();

        note.setNoteTitle(request.getNoteTitle());
        note.setPriority(request.getPriority());
        note.setNoteStatus(request.getNoteStatus());
        note.setPinned(request.isPinned());
        note.setDescription(request.getDescription());

        return saveNote(note);
    }

    @Override
    public Result updateNoteStatus(int noteId, NoteStatusUpdateRequest request) {
        DataResult<Note> noteResult = getNoteById(noteId);
        if (!noteResult.isSuccess()){
            return new ErrorResult(noteResult.getMessage());
        }

        Note note = (Note) noteResult.getData();

        if(note.getNoteStatus().equals(request.getNoteStatus())){
            return new SuccessResult("Note status is already "+ request.getNoteStatus().name());
        }
        note.setNoteStatus(request.getNoteStatus());

        Result result = saveNote(note);
        if (!result.isSuccess()){
            return result;
        }

        return new SuccessResult("Note status updated");
    }

    @Override
    public Result deleteNoteById(int noteId) {
        User authUser = authUserUtil.getAuthenticatedUser();
        if(authUser == null){
            return new ErrorResult("User not authenticated!");
        }

        DataResult<Note> noteResult = getNoteById(noteId);
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

    private DataResult<Note> getNoteById(int noteId){
        Note note = noteRepository.findById(noteId).orElse(null);
        if(note == null){
            return new ErrorDataResult<>("Note not found by id: " + noteId);
        }
        return new SuccessDataResult<>(note, "Note found!");
    }
}
