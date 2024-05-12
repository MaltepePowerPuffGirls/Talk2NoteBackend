package com.Talk2Note.Talk2NoteBackend.controller;

import com.Talk2Note.Talk2NoteBackend.api.controller.NoteController;
import com.Talk2Note.Talk2NoteBackend.api.dto.*;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
import com.Talk2Note.Talk2NoteBackend.core.enums.Priority;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.core.results.SuccessDataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.SuccessResult;
import com.Talk2Note.Talk2NoteBackend.core.utils.AuthUserUtil;
import com.Talk2Note.Talk2NoteBackend.core.utils.DtoMapUtil;
import com.Talk2Note.Talk2NoteBackend.entity.Member;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.MemberRepository;
import com.Talk2Note.Talk2NoteBackend.repository.NoteRepository;
import com.Talk2Note.Talk2NoteBackend.repository.TextBlockRepository;
import com.Talk2Note.Talk2NoteBackend.repository.UserRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.NoteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private NoteController noteController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepository;

    @MockBean
    private TextBlockRepository textBlockRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthUserUtil authUserUtil;

    private User authUser;
    private TextBlock textBlock;
    private Member member;
    private Note note;
    private List<Note> notes;
    private DataResult<List<NoteResponse>> result;
    private NoteResponse noteResponse;
    private TextBlockResponse textBlockResponse;
    private MemberResponse memberResponse;

    @Before
    public void setup() {

        authUser = User.builder()
                .email("user1@gmail.com")
                .password("test_hashed_password")
                .build();

        textBlock = TextBlock.builder()
                .rawText("Test raw text")
                .meaningfulText("Test meaningful text")
                .mdText("Test md text")
                .build();

        member = Member.builder()
                .user(authUser)
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
        note.setMembers(List.of(member));
        textBlock.setNote(note);

        userRepository.save(authUser);
        noteRepository.save(note);
        textBlockRepository.save(textBlock);
        memberRepository.save(member);

        notes = List.of(note);

        result = new SuccessDataResult<>(
                DtoMapUtil.generateNoteResponses(notes),
                "All notes fetched for specified user!"
        );

        noteResponse = DtoMapUtil.generateNoteResponse(note);
        textBlockResponse = DtoMapUtil.generateTextBlockResponse(textBlock);
        memberResponse = DtoMapUtil.generateMemberResponse(member);

        Mockito.when(authUserUtil.getAuthenticatedUser()).thenReturn(authUser);
    }

    @Test
    public void NoteController_getAllNotesByAuthUser_ReturnsAuthUserNotes(){
        Mockito.when(noteService.getAllNotesByAuthUser()).thenReturn(result);

        ResponseEntity<DataResult<List<NoteResponse>>> responseEntity = noteController.getAllNotesByAuthUser();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        DataResult<List<NoteResponse>> dataResult = responseEntity.getBody();
        assertNotNull(dataResult);
        assertTrue(dataResult.isSuccess());

        List<NoteResponse> notes = dataResult.getData();
        assertNotNull(notes);
        assertFalse(notes.isEmpty());
        assertEquals(notes.size(), notes.size());
    }

    @Test
    public void NoteController_getNoteById_ReturnsNote() {
        DataResult<NoteResponse> result = new SuccessDataResult<>(noteResponse, "Note fetched");

        Mockito.when(noteService.getNoteResponseById(Mockito.anyInt())).thenReturn(result);

        int noteId = 1;

        ResponseEntity<DataResult<NoteResponse>> responseEntity = noteController.getNoteById(noteId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        DataResult<NoteResponse> dataResult = responseEntity.getBody();
        assertNotNull(dataResult);
        assertTrue(dataResult.isSuccess());

        NoteResponse noteResponse = dataResult.getData();
        assertNotNull(noteResponse);
        assertEquals(noteId, noteResponse.getId());
    }

    @Test
    public void NoteController_getNoteBlocks_ReturnsTextBlocks(){
        int noteId = 1;
        DataResult<List<TextBlockResponse>> result = new SuccessDataResult<>(
                List.of(textBlockResponse),"All textblocks fetched for note: "+ noteId);

        Mockito.when(noteService.getNoteBlocks(Mockito.anyInt())).thenReturn(result);

        ResponseEntity<DataResult<List<TextBlockResponse>>> responseEntity = noteController.getNoteBlocks(noteId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        DataResult<List<TextBlockResponse>> dataResult = responseEntity.getBody();
        assertNotNull(dataResult);
        assertTrue(dataResult.isSuccess());

        List<TextBlockResponse> textBlockResponses = dataResult.getData();
        assertNotNull(textBlockResponses);
    }

    @Test
    public void NoteController_getNoteMembers_ReturnsMembers(){
        int noteId = 1;
        DataResult<List<MemberResponse>> result = new SuccessDataResult<>(
            List.of(memberResponse), "All members fetched for note: "+ noteId
        );

        Mockito.when(noteService.getNoteMembers(Mockito.anyInt())).thenReturn(result);

        ResponseEntity<DataResult<List<MemberResponse>>> responseEntity = noteController.getNoteMembers(noteId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        DataResult<List<MemberResponse>> dataResult = responseEntity.getBody();
        assertNotNull(dataResult);
        assertTrue(dataResult.isSuccess());

        List<MemberResponse> memberResponses = dataResult.getData();
        assertNotNull(memberResponses);
    }

    @Test
    public void NoteController_createNote_ReturnsSuccessResult(){
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);

        NoteCreateRequest request = NoteCreateRequest.builder()
                .noteTitle(note.getNoteTitle())
                .priority(note.getPriority())
                .description(note.getDescription())
                .build();

        ResponseEntity<Result> responseEntity = noteController.createNote(request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Result result = responseEntity.getBody();
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Note saved!", result.getMessage());
    }

    @Test
    public void NoteController_createTextBlock_ReturnsSuccessResult(){
        Mockito.when(noteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(note));
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);

        int noteId = 1;
        TextBlockCreateRequest request = TextBlockCreateRequest.builder()
                .rawText(textBlock.getRawText())
                .meaningfulText(textBlock.getMeaningfulText())
                .mdText(textBlock.getMdText())
                .build();

        ResponseEntity<Result> responseEntity = noteController.createTextBlock(noteId, request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Result result = responseEntity.getBody();
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Note saved!", result.getMessage());
    }

    @Test
    public void NoteController_editNote_ReturnsSuccessResult(){
        Mockito.when(noteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(note));
        Mockito.when(noteRepository.save(Mockito.any(Note.class))).thenReturn(note);

        int noteId = 1;
        NoteEditRequest request = NoteEditRequest.builder()
                .noteTitle("Edited test title")
                .description("Edited test description")
                .priority(Priority.HIGH)
                .noteStatus(NoteStatus.RECORDING)
                .pinned(true)
                .build();

        ResponseEntity<Result> responseEntity = noteController.editNote(noteId, request);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Result result = responseEntity.getBody();
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Note saved!", result.getMessage());
    }

    @Test
    public void NoteController_deleteNote_ReturnsSuccessResult(){
        Mockito.when(noteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(note));
        Mockito.when(noteRepository.existsById(Mockito.anyInt())).thenReturn(true);

        int noteId = 1;

        ResponseEntity<Result> responseEntity = noteController.deleteNote(noteId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Result result = responseEntity.getBody();
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Note deleted!", result.getMessage());
    }
}
