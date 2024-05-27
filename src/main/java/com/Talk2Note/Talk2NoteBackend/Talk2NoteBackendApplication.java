package com.Talk2Note.Talk2NoteBackend;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
public class Talk2NoteBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Talk2NoteBackendApplication.class, args);
	}

}
