package com.Talk2Note.Talk2NoteBackend.controller;

import com.Talk2Note.Talk2NoteBackend.api.controller.MemberController;
import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
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
import com.Talk2Note.Talk2NoteBackend.repository.TokenRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.DataTruncation;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUserUtil authUserUtil;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private MemberService memberService;

    private User authUser;
    private Note note;
    private Member member;
    private MemberResponse memberResponse;
    private String baseUri = "/api/v1/member";

    @Before
    public void setup() {
        authUser = User.builder()
                .email("user1@gmail.com")
                .password("test_hashed_password")
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
                .id(1)
                .note(note)
                .user(authUser)
                .build();

        note.setMembers(List.of(member));

        memberResponse = DtoMapUtil.generateMemberResponse(member);
    }

    @Test
    public void MemberController_getMemberResponseById_ReturnsMemberResponse() throws Exception {
        int memberId = 1;
        DataResult<MemberResponse> response = new SuccessDataResult<>(memberResponse, "Member fetched");

        Mockito.when(memberService.getMemberResponseById(Mockito.anyInt())).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{member-id}", memberId)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("Member fetched"),
                jsonPath("$.data").exists(),
                jsonPath("$.data.id").value(memberResponse.getId())
        );
    }
    /*
    url şeması çakışmasından dolayı çalışmadı member controllerdaki url şemasını kontrol et
    @Test
    public void MemberController_getAllMembersByUserId_ReturnsMembersList() throws Exception {
        int memberId = 1;
        DataResult<List<MemberResponse>> response = new SuccessDataResult<>(
                List.of(memberResponse), "All members fetched for specified user!");

        Mockito.when(memberService.getAllMembersByUserId(Mockito.anyInt())).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{user-id}", memberId)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("All members fetched for specified user!"),
                jsonPath("$.data").exists(),
                jsonPath("$.data[0].id").value(memberResponse.getId())
        );
    }
    */

    @Test
    public void MemberController_deleteMemberById_ReturnsSuccessResult() throws Exception {
        int memberId = 1;
        Result result = new SuccessResult("Member deleted!");

        Mockito.when(memberService.deleteMemberById(Mockito.anyInt())).thenReturn(result);

        mockMvc.perform(delete(baseUri + "/{member-id}", memberId)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("Member deleted!")
        );
    }

    @Test
    public void MemberController_getMemberResponseById_ReturnsBadRequest() throws Exception {
        int invalidMemberId = 999999;
        DataResult<MemberResponse> response = new ErrorDataResult<>("Member not found by id: " + invalidMemberId);

        Mockito.when(memberService.getMemberResponseById(invalidMemberId)).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{member-id}", invalidMemberId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("Member not found by id: " + invalidMemberId)
        );
    }

    /*
    @Test
    public void MemberController_getAllMembersByUserId_ReturnsBadRequest() throws Exception {
        int invalidUserId = 999999;
        DataResult<List<MemberResponse>> response = new ErrorDataResult<>("User not found by id: " + invalidUserId);

        Mockito.when(memberService.getAllMembersByUserId(invalidUserId)).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{user-id}", invalidUserId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("User not found by id: " + invalidUserId)
        );
    }
    */

    @Test
    public void MemberController_deleteMemberById_ReturnsBadRequest_ForInvalidMemberId() throws Exception {
        int invalidMemberId = 999999;
        Result result = new ErrorResult("Member not found by id: " + invalidMemberId);

        Mockito.when(memberService.deleteMemberById(invalidMemberId)).thenReturn(result);

        mockMvc.perform(delete(baseUri + "/{member-id}", invalidMemberId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("Member not found by id: " + invalidMemberId)
        );
    }
}
