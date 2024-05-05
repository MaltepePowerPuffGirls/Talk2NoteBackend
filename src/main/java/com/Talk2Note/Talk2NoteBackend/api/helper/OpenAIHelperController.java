package com.Talk2Note.Talk2NoteBackend.api.helper;

import com.Talk2Note.Talk2NoteBackend.core.enums.OpenAIRoleType;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/tester/openai")
@RequiredArgsConstructor
public class OpenAIHelperController {

    private final OpenAIService openAIService;

    @PostMapping("/chat")
    public ResponseEntity<String> chatByRole(@RequestBody Map<Integer, String> chat, @RequestParam OpenAIRoleType type) {

        String response = openAIService.chatByRole(chat, type);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/parsed-chat")
    public ResponseEntity<Map<Integer, String>> chatByRoleParsed(@RequestBody Map<Integer, String> chat, @RequestParam OpenAIRoleType type) {

        Map<Integer, String> response = openAIService.chatByRoleParsed(chat, type);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/algo/generate")
    public ResponseEntity<String> generatePrompt(@RequestBody Map<Integer, String> content) {

        String response = openAIService.generatePrompt(content);
        return ResponseEntity.ok(response);

    }


    @PostMapping("/algo/convert")
    public ResponseEntity<Map<Integer, String>> convertGeneratedResponse(@RequestBody OpenAIHelperConvertRequest request) {

        String message = request.getMessage();
        Map<Integer, String> response = openAIService.convertGeneratedResponse(message);
        return ResponseEntity.ok(response);

    }

}
