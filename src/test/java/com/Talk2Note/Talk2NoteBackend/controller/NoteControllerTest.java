package com.Talk2Note.Talk2NoteBackend.controller;

import com.Talk2Note.Talk2NoteBackend.api.controller.NoteController;
import com.Talk2Note.Talk2NoteBackend.api.dto.*;
import com.Talk2Note.Talk2NoteBackend.config.JwtService;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
import com.Talk2Note.Talk2NoteBackend.core.enums.Priority;
import com.Talk2Note.Talk2NoteBackend.core.results.*;
import com.Talk2Note.Talk2NoteBackend.core.utils.AuthUserUtil;
import com.Talk2Note.Talk2NoteBackend.core.utils.DtoMapUtil;
import com.Talk2Note.Talk2NoteBackend.entity.Member;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.*;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "com.Talk2Note.Talk2NoteBackend.config")
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService;
    @MockBean
    private AuthUserUtil authUserUtil;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private NoteRepository noteRepository;
    @MockBean
    private TextBlockRepository textBlockRepository;
    @MockBean
    private MemberRepository memberRepository;

    private User authUser;
    private Note note;
    private TextBlock textBlock;
    private Member member;
    private String baseUri = "/api/v1/note";

    @Before
    public void setup(){
        authUser = User.builder()
                .email("user1@gmail.com")
                .password("test_hashed_password")
                .build();

        textBlock = TextBlock.builder()
                .rawText("Test raw text")
                .meaningfulText("Test meaningfull text")
                .mdText("Test md text")
                .build();

        note = Note.builder()
                .id(1)
                .noteTitle("Test title")
                .priority(Priority.HIGH)
                .noteStatus(NoteStatus.RECORDING)
                .description("Test Description")
                .author(authUser)
                .build();

        member = Member.builder()
                .note(note)
                .user(authUser)
                .build();

        note.setTextBlocks(List.of(textBlock));
        textBlock.setNote(note);
        note.setMembers(List.of(member));

        userRepository.save(authUser);
        noteRepository.save(note);
        textBlockRepository.save(textBlock);
        memberRepository.save(member);

        Mockito.when(authUserUtil.getAuthenticatedUser()).thenReturn(authUser);
    }

    @Test
    public void NoteController_getAllNotesByAuthUser_ReturnsAuthUserNotes() throws Exception {
        NoteResponse noteResponse = DtoMapUtil.generateNoteResponse(note);
        DataResult<List<NoteResponse>> response = new SuccessDataResult<>(
                List.of(noteResponse),"All notes fetched for specified user!");

        Mockito.when(noteService.getAllNotesByAuthUser()).thenReturn(response);

        mockMvc.perform(get(baseUri)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("All notes fetched for specified user!"),
                jsonPath("$.data").exists(),
                jsonPath("$.data").isNotEmpty()
        );
    }

    @Test
    public void NoteController_getNoteById_ReturnsNoteResponse() throws Exception {
        int noteId = 1;
        NoteResponse noteResponse = DtoMapUtil.generateNoteResponse(note);
        DataResult<NoteResponse> response = new SuccessDataResult<>(noteResponse, "Note fetched");

        Mockito.when(noteService.getNoteResponseById(Mockito.anyInt()))
                .thenReturn(response);

        mockMvc.perform(get(baseUri + "/{note-id}", noteId)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("Note fetched"),
                jsonPath("$.data").exists(),
                jsonPath("$.data").isNotEmpty(),
                jsonPath("$.data.id").value(noteResponse.getId())
        );
    }

    @Test
    public void NoteController_getNoteBlocks_ReturnsNoteBlocks() throws Exception {
        int noteId = 1;
        TextBlockResponse textBlockResponse = DtoMapUtil.generateTextBlockResponse(textBlock);
        DataResult<List<TextBlockResponse>> response = new SuccessDataResult<>(
                List.of(textBlockResponse), "All textblocks fetched for note: "+ noteId
        );

        Mockito.when(noteService.getNoteBlocks(Mockito.anyInt())).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{note-id}/block", noteId)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("All textblocks fetched for note: "+ noteId),
                jsonPath("$.data").exists(),
                jsonPath("$.data").isNotEmpty(),
                jsonPath("$.data").isArray(),
                jsonPath("$.data[0].id").value(textBlockResponse.getId())
        );

    }

    @Test
    public void NoteController_getNoteMembers_ReturnsNoteMembers() throws Exception {
        int noteId = 1;
        MemberResponse memberResponse = DtoMapUtil.generateMemberResponse(member);
        DataResult<List<MemberResponse>> response = new SuccessDataResult<>(
          List.of(memberResponse), "All members fetched for note: "+ noteId
        );

        Mockito.when(noteService.getNoteMembers(Mockito.anyInt())).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{note-id}/member", noteId)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("All members fetched for note: "+ noteId),
                jsonPath("$.data").exists(),
                jsonPath("$.data").isNotEmpty(),
                jsonPath("$.data").isArray(),
                jsonPath("$.data[0].id").value(memberResponse.getId())
        );
    }

    @Test
    public void NoteController_createNote_ReturnsSuccessResult() throws Exception {

        NoteCreateRequest noteCreateRequest = NoteCreateRequest.builder()
                .noteTitle(note.getNoteTitle())
                .description(note.getDescription())
                .priority(note.getPriority())
                .noteType(note.getNoteType())
                .build();
        Result result = new SuccessResult("Note saved!");

        Mockito.when(noteService.createNote(noteCreateRequest)).thenReturn(result);

        mockMvc.perform(post(baseUri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(noteCreateRequest)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("Note saved!")
                );
    }

    @Test
    public void NoteController_createTextBlock_ReturnsSuccessResult() throws Exception {
        int noteId = 1;
        TextBlockCreateRequest textBlockCreateRequest = TextBlockCreateRequest.builder()
                .rawText(textBlock.getRawText())
                .meaningfulText(textBlock.getMeaningfulText())
                .mdText(textBlock.getMdText())
                .build();

        Result result = new SuccessResult("Note saved!");

        Mockito.when(noteService.addTextBlock(noteId, textBlockCreateRequest)).thenReturn(result);

        mockMvc.perform(post(baseUri + "/{note-id}/block", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(textBlockCreateRequest)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("Note saved!")
                );
    }

    @Test
    public void NoteController_editNote_ReturnsSuccessResult() throws Exception {
        int noteId = 1;
        NoteEditRequest noteEditRequest = NoteEditRequest.builder()
                .noteTitle(note.getNoteTitle())
                .priority(note.getPriority())
                .noteStatus(note.getNoteStatus())
                .pinned(true)
                .description(note.getDescription())
                .build();

        Result result = new SuccessResult("Note saved!");

        Mockito.when(noteService.editNote(noteId, noteEditRequest)).thenReturn(result);

        mockMvc.perform(put(baseUri + "/{note-id}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteEditRequest)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("Note saved!")
                );
    }

    @Test
    public void NoteController_deleteNote_ReturnsSuccessResult() throws Exception {
        int noteId = 1;
        Result result = new SuccessResult("Note deleted!");

        Mockito.when(noteService.deleteNoteById(Mockito.anyInt())).thenReturn(result);

        mockMvc.perform(delete(baseUri + "/{note-id}", noteId)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("Note deleted!")
        );
    }

    @Test
    public void NoteController_getAllNotesByAuthUser_ReturnsBadRequest() throws Exception {
        DataResult<List<NoteResponse>> response = new ErrorDataResult<>("User not authorized!");

        Mockito.when(noteService.getAllNotesByAuthUser()).thenReturn(response);

        mockMvc.perform(get(baseUri)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("User not authorized!")
        );
    }

    @Test
    public void NoteController_getNoteById_ReturnsNotFound() throws Exception {
        int invalidNoteId = 999999999;
        DataResult<NoteResponse> response = new ErrorDataResult<>("Note not found by id: " + invalidNoteId);

        Mockito.when(noteService.getNoteResponseById(invalidNoteId)).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{note-id}", invalidNoteId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("Note not found by id: " + invalidNoteId)
        );

    }

    @Test
    public void NoteController_getNoteBlocks_ReturnsBadRequest_ForInvalidNoteId() throws Exception {
        int invalidNoteId = 999999999;
        DataResult<List<TextBlockResponse>> response = new ErrorDataResult<>("Note not found by id: " + invalidNoteId);

        Mockito.when(noteService.getNoteBlocks(invalidNoteId)).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{note-id}/block", invalidNoteId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("Note not found by id: " + invalidNoteId)
        );

    }

    @Test
    public void NoteController_getNoteMembers_ReturnsBadRequest_ForInvalidNoteId() throws Exception {
        int invalidNoteId = 999999999;
        DataResult<List<MemberResponse>> response = new ErrorDataResult<>("Note not found by id: " + invalidNoteId);

        Mockito.when(noteService.getNoteMembers(invalidNoteId)).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{note-id}/member", invalidNoteId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("Note not found by id: " + invalidNoteId)
        );

    }

    @Test
    public void NoteController_createNote_ReturnsBadRequest_ForInvalidInput() throws Exception {
        NoteCreateRequest invalidRequest = new NoteCreateRequest();

        Result result = new ErrorResult("Invalid note data");

        Mockito.when(noteService.createNote(invalidRequest)).thenReturn(result);

        mockMvc.perform(post(baseUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Invalid note data")
                );
    }

    @Test
    public void NoteController_createTextBlock_ReturnsBadRequest_ForInvalidInput() throws Exception {
        int noteId = 1;
        TextBlockCreateRequest invalidRequest = new TextBlockCreateRequest();

        Result result = new ErrorResult("Invalid text block data");

        Mockito.when(noteService.addTextBlock(noteId, invalidRequest)).thenReturn(result);

        mockMvc.perform(post(baseUri + "/{note-id}/block", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Invalid text block data")
                );

    }

    @Test
    public void NoteController_editNote_ReturnsBadRequest_ForInvalidInput() throws Exception {
        int noteId = 1;
        NoteEditRequest invalidRequest = new NoteEditRequest();

        Result result = new ErrorResult("Invalid note data");

        Mockito.when(noteService.editNote(noteId, invalidRequest)).thenReturn(result);

        mockMvc.perform(put(baseUri + "/{note-id}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Invalid note data")
                );
    }

    @Test
    public void NoteController_deleteNote_ReturnsBadRequest_ForInvalidNoteId() throws Exception {
        int invalidNoteId = 999999999;
        Result result = new ErrorResult("Note not found by id: " + invalidNoteId);

        Mockito.when(noteService.deleteNoteById(invalidNoteId)).thenReturn(result);

        mockMvc.perform(delete(baseUri + "/{note-id}", invalidNoteId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("Note not found by id: " + invalidNoteId)
        );

    }

}
