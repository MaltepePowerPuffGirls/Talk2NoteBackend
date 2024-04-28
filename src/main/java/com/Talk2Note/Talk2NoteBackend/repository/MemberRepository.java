package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.entity.Member;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    @Query(value = """
            select m from Member m\s
            where m.user = :user\s
            """)
    List<Member> findAllMembersByUser(User user);

    @Query(value = """
            select m from Member m\s
            where m.note = :note\s
            """)
    List<Member> findAllMembersByNote(Note note);
}
