package com.Talk2Note.Talk2NoteBackend.service.concretes;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.*;
import com.Talk2Note.Talk2NoteBackend.core.utils.AuthUserUtil;
import com.Talk2Note.Talk2NoteBackend.entity.Member;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.MemberRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.MemberService;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.Talk2Note.Talk2NoteBackend.core.utils.DtoMapUtil.generateMemberResponse;
import static com.Talk2Note.Talk2NoteBackend.core.utils.DtoMapUtil.generateMemberResponses;

@Service
@RequiredArgsConstructor
public class MemberManager implements MemberService {
    private final MemberRepository memberRepository;
    private final UserService userService;
    private final AuthUserUtil authUserUtil;

    @Override
    public DataResult<Member> getMemberById(int memberId){
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null){
            return new ErrorDataResult<>("Member not found by id: " + memberId);
        }
        return new SuccessDataResult<>(member,"Member found!");
    }

    @Override
    public DataResult<MemberResponse> getMemberResponseById(int memberId) {
        DataResult<Member> memberResult = getMemberById(memberId);
        if (!memberResult.isSuccess()){
            return new ErrorDataResult<>(memberResult.getMessage());
        }
        Member member = (Member) memberResult.getData();
        MemberResponse response = generateMemberResponse(member);
        return new SuccessDataResult<>(response, "Member fetched");
    }

    @Override
    public DataResult<List<MemberResponse>> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        List<MemberResponse> memberResponses = generateMemberResponses(members);

        return new SuccessDataResult<>(memberResponses, "All members fetched");
    }

    @Override
    public DataResult<List<MemberResponse>> getAllMembersByUserId(int userId) {
        DataResult<User> userResult = userService.getUserById(userId);
        if (!userResult.isSuccess()) {
            return new ErrorDataResult<>(userResult.getMessage());
        }
        User user = (User) userResult.getData();

        List<Member> members = memberRepository.findAllMembersByUser(user);
        List<MemberResponse> response = generateMemberResponses(members);

        return new SuccessDataResult<>(response, "All members fetched for specified user!");
    }

    @Override
    public Result deleteMemberById(int memberId) {
        User authUser = authUserUtil.getAuthenticatedUser();
        if(authUser == null){
            return new ErrorResult("User not authenticated!");
        }

        DataResult<Member> memberResult = getMemberById(memberId);
        if (!memberResult.isSuccess()){
            return new ErrorResult("TextBlock not found by id: "+ memberId);
        }
        Member member = (Member) memberResult.getData();

        if(member.getNote().getAuthor() != authUser){
            return new ErrorResult("User not authorized for member deletion!");
        }
        return deleteMember(member);
    }

    @Override
    public Result saveMember(Member member) {
        try {
            memberRepository.save(member);
        }catch (Exception e){
            return new ErrorResult("Unexpected Error Occurred: " + e.getMessage());
        }
        return new SuccessResult("Member saved!");
    }

    private Result deleteMember(Member member) {
        try {
            memberRepository.delete(member);
        }catch (Exception e){
            return new ErrorResult(e.getMessage());
        }
        return new SuccessResult("Member deleted!");
    }

}
