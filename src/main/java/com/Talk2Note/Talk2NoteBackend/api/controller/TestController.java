package com.Talk2Note.Talk2NoteBackend.api.controller;

import com.Talk2Note.Talk2NoteBackend.repository.NoteRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final NoteService noteService;
    private final NoteRepository noteRepository;

    @GetMapping("/openai")
    public void openaiTest() {

        noteRepository.findById(1).ifPresent(noteService::updateNote);

    }


}
