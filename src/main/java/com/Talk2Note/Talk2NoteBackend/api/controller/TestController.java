package com.Talk2Note.Talk2NoteBackend.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("/hello-word")
    public ResponseEntity<HashMap<String, String>> testHelloWorld() {
        HashMap res = new HashMap(){};
        res.put("message", "Hello, World!");
        return ResponseEntity.ok(res);
    }
}
