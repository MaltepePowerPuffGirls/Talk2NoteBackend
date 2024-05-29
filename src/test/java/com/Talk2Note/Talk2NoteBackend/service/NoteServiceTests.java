package com.Talk2Note.Talk2NoteBackend.service;

import com.Talk2Note.Talk2NoteBackend.api.dto.*;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
import com.Talk2Note.Talk2NoteBackend.core.enums.Priority;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.core.utils.AuthUserUtil;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.NoteRepository;
import com.Talk2Note.Talk2NoteBackend.repository.TextBlockRepository;
import com.Talk2Note.Talk2NoteBackend.repository.UserRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.NoteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteServiceTests {

    @Autowired
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepository;

    @MockBean
    private TextBlockRepository textBlockRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthUserUtil authUserUtil;

    private User authUser;
    private TextBlock textBlock;
    private Note note;

    @Before
    public void setup() {

        authUser = User.builder()
                .email("user1@gmail.com")
                .password("test_hashed_password")
                .build();

        textBlock = TextBlock.builder()
                .rawText("Test raw text")
                .meaningfulText("Test meaningful text")
                .build();

        note = Note.builder()
                .id(1)
                .noteTitle("Edited test title")
                .priority(Priority.HIGH)
                .noteStatus(NoteStatus.RECORDING)
                .pinned(true)
                .description("Test Description")
                .author(authUser)
                .build();

        note.setTextBlocks(List.of(textBlock));
        textBlock.setNote(note);

        userRepository.save(authUser);
        noteRepository.save(note);
        textBlockRepository.save(textBlock);

        Mockito.when(authUserUtil.getAuthenticatedUser()).thenReturn(authUser);

    }

    @Test
    public void NoteManager_createNote_ReturnsSuccessResult() {
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);

        NoteCreateRequest request = new NoteCreateRequest();
        request.setNoteTitle(note.getNoteTitle());
        request.setPriority(note.getPriority());
        request.setDescription(note.getDescription());

        Result result = noteService.createNote(request);

        assertTrue(result.isSuccess());
        assertEquals("Note saved!", result.getMessage());
    }

    @Test
    public void NoteManager_getNoteById_ReturnsNoteData() {
        Mockito.when(noteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(note));

        int noteId = 1;

        DataResult<NoteResponse> result = noteService.getNoteResponseById(noteId);

        assertTrue(result.isSuccess());
        assertEquals("Note fetched", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(noteId, result.getData().getId());
    }

    @Test
    public void NoteManager_deleteNoteById_ReturnsSuccessResult() {
        int noteId = 1;
        Mockito.when(noteRepository.existsById(noteId)).thenReturn(true);

        Result result = noteService.deleteNoteById(noteId);

        assertTrue(result.isSuccess());
        assertEquals("Note deleted!", result.getMessage());
    }

    @Test
    public void NoteManager_addTextBlock_ReturnsSuccessResult() {
        int noteId = 1;
        TextBlockCreateRequest request = new TextBlockCreateRequest();
        request.setRawText(textBlock.getRawText());

        Mockito.when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);

        Result result = noteService.addTextBlock(noteId, request);

        assertTrue(result.isSuccess());
        assertEquals("Note saved!", result.getMessage());
    }

    @Test
    public void NoteManager_editNote_ReturnsSuccessResult() {
        int noteId = 1;
        NoteEditRequest request = new NoteEditRequest();
        request.setNoteTitle("Edited test title");
        request.setPriority(Priority.HIGH);
        request.setNoteStatus(NoteStatus.RECORDING);
        request.setPinned(true);
        request.setDescription("Edited test description");

        Mockito.when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);

        Result result = noteService.editNote(noteId, request);

        assertTrue(result.isSuccess());
        assertEquals("Note saved!", result.getMessage());
    }

    @Test
    public void NoteManager_getAllNotesByAuthUser_ReturnsNotesForAuthenticatedUser() {
        Mockito.when(noteRepository.findAllNotesByUser(Mockito.any(User.class))).thenReturn(Collections.singletonList(note));

        DataResult<List<NoteResponse>> result = noteService.getAllNotesByAuthUser();

        assertTrue(result.isSuccess());
        assertEquals("All notes fetched for specified user!", result.getMessage());
        assertEquals(1, result.getData().size());
        assertEquals(note, result.getData().get(0));
    }

    @Test
    public void NoteManager_getNoteBlocks_ReturnsTextBlocksForNote() {
        int noteId = 1;
        List<TextBlock> textBlocks = Collections.singletonList(TextBlock.builder().build());
        Mockito.when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        DataResult<List<TextBlockResponse>> result = noteService.getNoteBlocks(noteId);

        assertTrue(result.isSuccess());
        assertEquals("All textblocks fetched for note: " + noteId, result.getMessage());
        assertEquals(textBlocks.size(), result.getData().size());
    }

    @Test
    public void NoteManager_getNoteMembers_ReturnsMembersForNote() {
        int noteId = 1;
        List<MemberResponse> memberResponses = Collections.singletonList(MemberResponse.builder().build());
        Mockito.when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        DataResult<List<MemberResponse>> result = noteService.getNoteMembers(noteId);

        assertTrue(result.isSuccess());
        assertEquals("All members fetched for note: " + noteId, result.getMessage());
        assertEquals(memberResponses.size(), result.getData().size());
    }

}
