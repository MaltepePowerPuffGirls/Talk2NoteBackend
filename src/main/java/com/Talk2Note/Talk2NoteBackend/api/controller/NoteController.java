package com.Talk2Note.Talk2NoteBackend.api.controller;

import com.Talk2Note.Talk2NoteBackend.api.dto.*;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notes")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Get all notes for authuser", description = "Get all notes for authenticated user")
    public ResponseEntity<DataResult<List<Note>>> getAllNotesByAuthUser(){
        DataResult<List<Note>> result = noteService.getAllNotesByAuthUser();
        if (!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{note-id}")
    @Operation(summary = "Get a note", description = "Get a specific note")
    public ResponseEntity<DataResult<NoteResponse>> getNoteById(
            @PathVariable(name = "note-id") int noteId
    ){
        DataResult<NoteResponse> result = noteService.getNoteById(noteId);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{note-id}/blocks")
    @Operation(summary = "Get note's blocks", description = "Get the specified note's textblocks")
    public ResponseEntity<DataResult<List<TextBlockResponse>>> getNoteBlocks(
            @PathVariable(name = "note-id") int noteId
    ){
        DataResult<List<TextBlockResponse>> result = noteService.getNoteBlocks(noteId);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{note-id}/members")
    @Operation(summary = "Get note's members", description = "Get the specified note's members")
    public ResponseEntity<DataResult<List<MemberResponse>>> getNoteMembers(
            @PathVariable(name = "note-id") int noteId
    ){
        DataResult<List<MemberResponse>> result = noteService.getNoteMembers(noteId);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create new note", description = "Create a new note")
    public ResponseEntity<Result> createNote(
            @RequestBody NoteCreateRequest request
    ){
        Result result = noteService.createNote(request);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{note-id}/block")
    @Operation(summary = "Create text block", description = "Create a new text block in the specified note")
    public ResponseEntity<Result> createTextBlock(
            @PathVariable(name = "note-id") int noteId,
            @RequestBody TextBlockCreateRequest request
    ){
        Result result = noteService.addTextBlock(noteId, request);
        if (!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{note-id}")
    @Operation(summary = "Edit Note", description = "Edit a note")
    public ResponseEntity<Result> editNote(
            @PathVariable(name = "note-id") int noteId,
            @RequestBody NoteEditRequest request
    ){
        Result result = noteService.editNote(noteId, request);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{note-id}")
    @Operation(summary = "Delete Note", description = "Delete a note")
    public ResponseEntity<Result> deleteNote(
            @PathVariable(name = "note-id") int noteId
    ){
        Result result = noteService.deleteNoteById(noteId);
        if(!result.isSuccess()){
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}
