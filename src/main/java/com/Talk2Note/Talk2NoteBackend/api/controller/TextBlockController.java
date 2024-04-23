package com.Talk2Note.Talk2NoteBackend.api.controller;

import com.Talk2Note.Talk2NoteBackend.api.dto.NoteResponse;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockCreateRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockEditRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.TextBlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/text-block")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class TextBlockController {
    private final TextBlockService textBlockService;

    @GetMapping("/{block-id}")
    @Operation(summary = "Get text block", description = "Get the text block by id")
    public ResponseEntity<DataResult<TextBlockResponse>> getTextBlock(
            @PathVariable(name = "block-id") int blockId
    ){
        DataResult<TextBlockResponse> result = textBlockService.getTextBlockById(blockId);
        if (!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{block-id}")
    public ResponseEntity<Result> editTextBlock(
            @PathVariable(name = "block-id") int blockId,
            @RequestBody TextBlockEditRequest request
    ){
        Result result = textBlockService.editTextBlock(blockId, request);
        if (!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{block-id}")
    public ResponseEntity<Result> deleteTextBlock(
            @PathVariable(name = "block-id") int blockId
    ){
        Result result = textBlockService.deleteTextBlockById(blockId);
        if (!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
