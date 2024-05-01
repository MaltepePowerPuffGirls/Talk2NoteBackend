package com.Talk2Note.Talk2NoteBackend.service.abstracts;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Member;

import java.util.List;

public interface MemberService {

    DataResult<Member> getMemberById(int memberId);
    DataResult<MemberResponse> getMemberResponseById(int memberId);

    DataResult<List<MemberResponse>> getAllMembers();

    DataResult<List<MemberResponse>> getAllMembersByUserId(int userId);

    // Result updateMember(MemberEditRequest request);

    Result deleteMemberById(int memberId);

    Result saveMember(Member member);
}
