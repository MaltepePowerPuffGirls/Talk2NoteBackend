package com.Talk2Note.Talk2NoteBackend.service.abstracts;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Member;

import java.util.List;

public interface MemberService {

    DataResult<MemberResponse> getMemberById(int memberId);

    DataResult<List<MemberResponse>> getAllMembers();

    DataResult<List<MemberResponse>> getAllMembersByUserId(int userId);

    // Result updateMember(MemberEditRequest request);

    Result deleteMemberById(int memberId);

    private Result saveMember(Member member){return null;}

    private Result deleteMember(Member member){return null;}

    private DataResult<Member> getMember(int memberId){return null;}

}
