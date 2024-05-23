package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.entity.Member;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    private User user1, user2;
    private Member member1, member2, member3;
    private Note note;

    @Before
    public void setup() {

        user1 = User.builder().email("user1@gmail.com").build();
        user2 = User.builder().email("user2@gmail.com").build();

        member1 = Member.builder().user(user1).build();
        member2 = Member.builder().user(user1).build();
        member3 = Member.builder().user(user2).build();

        note = Note.builder().noteTitle("Test Note Title").build();

        member1.setNote(note);
        member2.setNote(note);

        userRepository.save(user1);
        userRepository.save(user2);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        noteRepository.save(note);
    }

    @Test
    public void testFindAllMembersByUser_WithValidUser_ReturnsMembers() {

        List<Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(member1);
        expectedMembers.add(member2);

        assertEquals(member1.getUser().getEmail(), user1.getEmail());
        assertEquals(member1.getUser().getEmail(), member2.getUser().getEmail());

        List<Member> actualMembers = memberRepository.findAllMembersByUser(user1);

        assertEquals(expectedMembers.size(), actualMembers.size());
    }

    @Test
    public void testFindAllMembersByNote_WithValidNote_ReturnsMembers() {

        List<Member> expectedMembers = new ArrayList<>();
        expectedMembers.add(member1);
        expectedMembers.add(member2);

        List<Member> actualMembers = memberRepository.findAllMembersByNote(note);

        assertEquals(expectedMembers.size(), actualMembers.size());
    }

}
