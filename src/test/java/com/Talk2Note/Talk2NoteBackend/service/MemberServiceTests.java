package com.Talk2Note.Talk2NoteBackend.service;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Member;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.MemberRepository;
import com.Talk2Note.Talk2NoteBackend.repository.NoteRepository;
import com.Talk2Note.Talk2NoteBackend.repository.UserRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.MemberService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NoteRepository noteRepository;

    private User user;
    private Note note;
    private Member member;

    @Before
    public void setup() {

        user = User.builder().id(1).build();
        note = Note.builder().id(1).build();
        member = Member.builder()
                .id(1)
                .user(user)
                .note(note)
                .build();

        userRepository.save(user);
        noteRepository.save(note);
        memberRepository.save(member);

    }

    @Test
    public void MemberService_getMemberById_ReturnsMemberData() {

        DataResult<Member> result = memberService.getMemberById(1);
        assertTrue(result.isSuccess());
        assertEquals("Member fetched", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(member.getId(), result.getData().getId());

    }

    @Test
    public void MemberService_getAllMembers_ReturnsEmptyList() {

        DataResult<List<MemberResponse>> result = memberService.getAllMembers();
        assertTrue(result.isSuccess());
        assertEquals("All members fetched", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(0, result.getData().size());

    }

    @Test
    public void MemberService_getAllMembersByUserId_ReturnsMemberList() {

        DataResult<List<MemberResponse>> result = memberService.getAllMembersByUserId(1);
        assertTrue(result.isSuccess());
        assertEquals("All members fetched for specified user!", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(member.getId(), result.getData().get(0).getId());

    }

    @Test
    public void MemberService_deleteMemberById_ReturnsSuccessResult() {

        Result result = memberService.deleteMemberById(1);
        assertTrue(result.isSuccess());
        assertEquals("Member deleted!", result.getMessage());

    }

}
