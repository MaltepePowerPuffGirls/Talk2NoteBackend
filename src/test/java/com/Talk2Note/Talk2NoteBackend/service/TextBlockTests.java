package com.Talk2Note.Talk2NoteBackend.service;


import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockEditRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.repository.TextBlockRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.TextBlockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TextBlockTests {

    @Autowired
    private TextBlockService textBlockService;

    @Autowired
    private TextBlockRepository textBlockRepository;

    @Before
    public void setup() {

    }

    @Test
    public void TextBlockManager_getTextBlockById_ReturnsTextBlockData() {
        // Assuming there's a text block with ID 1 in the database
        int textBlockId = 1;

        // Call the method
        DataResult<TextBlockResponse> result = textBlockService.getTextBlockById(textBlockId);

        // Verify the result
        assertTrue(result.isSuccess());
        assertEquals("TextBlock fetched!", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(textBlockId, result.getData().getId());
    }

    @Test
    public void TextBlockManager_editTextBlock_ReturnsSuccessResult() {
        // Assuming there's a text block with ID 1 in the database
        int textBlockId = 1;

        // Prepare a TextBlockEditRequest
        TextBlockEditRequest request = new TextBlockEditRequest();
        request.setRowNumber(2);
        request.setRawText("Edited Raw Text");
        request.setMeaningfulText("Edited Meaningful Text");
        request.setMdText("Edited MD Text");

        // Call the method
        Result result = textBlockService.editTextBlock(textBlockId, request);

        // Verify the result
        assertTrue(result.isSuccess());
        assertEquals("TextBlock saved!", result.getMessage());
    }

    @Test
    public void TextBlockManager_deleteTextBlockById_ReturnsSuccessResult() {
        // Assuming there's a text block with ID 1 in the database
        int textBlockId = 1;

        // Call the method
        Result result = textBlockService.deleteTextBlockById(textBlockId);

        // Verify the result
        assertTrue(result.isSuccess());
        assertEquals("TextBlock deleted!", result.getMessage());
    }

}
