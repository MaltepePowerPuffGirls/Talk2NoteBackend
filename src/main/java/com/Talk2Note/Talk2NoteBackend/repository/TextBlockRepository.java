package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TextBlockRepository extends JpaRepository<TextBlock, Integer> {

    @Query(value = """
            select t from TextBlock t\s
            where t.note = :note\s
            """)
    List<TextBlock> findAllTextBlocksByNote(Note note);

    List<TextBlock> getAllByNoteOrderByRowNumberAsc(Note note);

}
