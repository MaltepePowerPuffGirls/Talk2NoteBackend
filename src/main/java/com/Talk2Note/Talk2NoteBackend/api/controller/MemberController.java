package com.Talk2Note.Talk2NoteBackend.api.controller;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/member")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{member-id}")
    @Operation(summary = "Get member", description = "Get a member by id")
    public ResponseEntity<DataResult<MemberResponse>> getMemberResponseById(
             @PathVariable(name = "member-id") int memberId
    ){
        DataResult<MemberResponse> result = memberService.getMemberResponseById(memberId);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    /*
    url şeması üstteki ile çakışıyor!
    @GetMapping("/{user-id}")
    @Operation(summary = "Get members for user", description = "Get members for specified userId")
    public ResponseEntity<DataResult<List<MemberResponse>>> getAllMembersByUserId(
            @PathVariable(name = "user-id") int userId
    ){
        DataResult<List<MemberResponse>> result = memberService.getAllMembersByUserId(userId);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
    */

    @DeleteMapping("/{member-id}")
    @Operation(summary = "Delete member", description = "Delete member by id")
    public ResponseEntity<Result> deleteMemberById(
            @PathVariable(name = "member-id") int memberId
    ){
        Result result = memberService.deleteMemberById(memberId);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
