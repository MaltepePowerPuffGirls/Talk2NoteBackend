package com.Talk2Note.Talk2NoteBackend.repository;

import com.Talk2Note.Talk2NoteBackend.core.enums.NoteStatus;
import com.Talk2Note.Talk2NoteBackend.core.enums.NoteType;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TextBlockRepositoryTest {

    @Autowired
    private TextBlockRepository textBlockRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    private TextBlock textBlock1;
    private TextBlock textBlock2;
    private User user;
    private Note note;


    @Before
    public void setup() {
        user = User.builder()
                .email("test@gmail.com")
                .password("test_passwd")
                .build();

        note = Note.builder()
                .author(user)
                .noteTitle("test note")
                .noteStatus(NoteStatus.RECORDING)
                .noteType(NoteType.DEVELOPER)
                .build();

        textBlock1 = TextBlock.builder()
                .rowNumber(1)
                .rawText("raw text")
                .meaningfulText("meaningful text")
                .note(note)
                .build();

        textBlock2 = TextBlock.builder()
                .rowNumber(2)
                .rawText("raw text")
                .meaningfulText("meaningful text")
                .note(note)
                .build();

        List<TextBlock> blocks = new ArrayList<>();
        blocks.add(textBlock1);
        blocks.add(textBlock2);
        note.setTextBlocks(blocks);

        userRepository.save(user);
        noteRepository.save(note);
    }

    // best cases
    @Test
    public void TextBlockRepository_saveBlock_ReturnsSavedBlock() {

        TextBlock block = textBlockRepository.save(textBlock1);

        assertNotNull(block);
        assertTrue(block.getId() > 0);
        assertEquals(textBlock1.getRowNumber(), block.getRowNumber());
        assertEquals(textBlock1.getRawText(), block.getRawText());
        assertEquals(textBlock1.getMeaningfulText(), block.getMeaningfulText());
    }

    @Test
    public void TextBlockRepository_findById_ReturnsBlock() {
        TextBlock block = textBlockRepository.save(textBlock1);
        int id = block.getId();

        TextBlock foundedBlock = textBlockRepository.findById(id).orElse(null);

        assertNotNull(foundedBlock);
        assertEquals(id, foundedBlock.getId());
    }

    @Test
    public void TextBlockRepository_findAll_ReturnsBlocks() {
        textBlockRepository.save(textBlock1);
        textBlockRepository.save(textBlock2);

        List<TextBlock> blocks = textBlockRepository.findAll();

        assertFalse(blocks.isEmpty());
        assertEquals(blocks.size(), 2);
    }

    @Test
    public void TextBlockRepository_findAllById_ReturnsBlocks() {
        textBlockRepository.save(textBlock1);
        textBlockRepository.save(textBlock2);

        List<Integer> ids = new ArrayList<>();
        ids.add(textBlock1.getId());
        ids.add(textBlock2.getId());

        List<TextBlock> blocks = textBlockRepository.findAllById(ids);

        assertFalse(blocks.isEmpty());
        assertEquals(blocks.size(), 2);
    }


    @Test
    public void TextBlockRepository_findAllByNote_ReturnsBlocks() {
        textBlockRepository.save(textBlock1);
        textBlockRepository.save(textBlock2);

        List<TextBlock> blocks = textBlockRepository.findAllTextBlocksByNote(note);

        assertFalse(blocks.isEmpty());
        assertEquals(blocks.size(), 2);
    }

    @Test
    public void TextBlockRepository_delete_ReturnsNothing() {
        TextBlock block = textBlockRepository.save(textBlock1);
        int id = block.getId();

        textBlockRepository.delete(block);

        TextBlock deletedBlock = textBlockRepository.findById(id).orElse(null);

        assertNull(deletedBlock);
    }

    @Test
    public void TextBlockRepository_deleteById_ReturnsNothing() {
        TextBlock block = textBlockRepository.save(textBlock1);
        int id = block.getId();

        textBlockRepository.deleteById(id);

        TextBlock deletedBlock = textBlockRepository.findById(id).orElse(null);

        assertNull(deletedBlock);
    }

    @Test
    public void TextBlockRepository_deleteAll_ReturnsNothing() {
        TextBlock block = textBlockRepository.save(textBlock1);

        textBlockRepository.deleteAll(List.of(block));

        List<TextBlock> blocks = textBlockRepository.findAll();

        assertTrue(blocks.isEmpty());
    }

    @Test
    public void TextBlockRepository_deleteAllById_ReturnsNothing() {
        TextBlock block = textBlockRepository.save(textBlock1);

        textBlockRepository.deleteAllById(List.of(block.getId()));

        List<TextBlock> blocks = textBlockRepository.findAllById(List.of(block.getId()));

        assertTrue(blocks.isEmpty());
    }

    //worst case
    @Test
    public void TextBlockRepository_findById_ReturnsNull_ForInvalidId() {
        int id = 999999;

        TextBlock foundedBlock = textBlockRepository.findById(id).orElse(null);

        assertNull(foundedBlock);
    }

    @Test
    public void TextBlockRepository_findAll_ReturnsEmptyList_WhenNotAnyBlockExists() {

        List<TextBlock> blocks = textBlockRepository.findAll();

        assertTrue(blocks.isEmpty());
    }

    @Test
    public void TextBlockRepository_findAllById_ReturnsEmptyList_WhenNotAnyBlockExistsForId() {

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);

        List<TextBlock> blocks = textBlockRepository.findAllById(ids);

        assertTrue(blocks.isEmpty());
    }

    @Test
    public void TextBlockRepository_findAllByNote_ReturnsEmptyList_WhenNotAnyBlockExists() {

        List<TextBlock> blocks = textBlockRepository.findAllTextBlocksByNote(note);

        assertTrue(blocks.isEmpty());
    }

    @Test
    public void TextBlockRepository_delete_NonExistentBlock_DoesNothing() {
        textBlockRepository.save(textBlock1);
        TextBlock block = TextBlock.builder().build();

        textBlockRepository.delete(block);
        assertEquals(1, textBlockRepository.count());
    }

    @Test
    public void TextBlockRepository_deleteById_NonExistentBlockId_DoesNothing() {
        textBlockRepository.save(textBlock1);
        TextBlock block = TextBlock.builder()
                .id(999)
                .build();

        textBlockRepository.deleteById(block.getId());
        assertEquals(1, textBlockRepository.count());
    }

    @Test
    public void TextBlockRepository_deleteAll_ReturnsNothing2() {
        textBlockRepository.save(textBlock1);
        TextBlock block = TextBlock.builder().build();

        textBlockRepository.deleteAll(List.of(block));

        assertEquals(1, textBlockRepository.count());
    }

    @Test
    public void TextBlockRepository_deleteAllById_ReturnsNothing1() {
        textBlockRepository.save(textBlock1);
        TextBlock block = TextBlock.builder()
                .id(999)
                .build();

        textBlockRepository.deleteAllById(List.of(block.getId()));

        assertEquals(1, textBlockRepository.count());
    }
}
