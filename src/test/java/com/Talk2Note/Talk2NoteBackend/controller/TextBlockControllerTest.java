package com.Talk2Note.Talk2NoteBackend.controller;

import com.Talk2Note.Talk2NoteBackend.api.controller.TextBlockController;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockEditRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
import com.Talk2Note.Talk2NoteBackend.config.JwtService;
import com.Talk2Note.Talk2NoteBackend.core.results.*;
import com.Talk2Note.Talk2NoteBackend.core.utils.AuthUserUtil;
import com.Talk2Note.Talk2NoteBackend.repository.TokenRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.TextBlockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = TextBlockController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class TextBlockControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUserUtil authUserUtil;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private TextBlockService textBlockService;
    private TextBlockResponse textBlockResponse;
    private String baseUri = "/api/v1/text-block";

    @Before
    public void setup() {
        textBlockResponse = TextBlockResponse.builder()
                .id(1)
                .rawText("Test raw text")
                .meaningfulText("Test meaningful text")
                .mdText("Test md text")
                .build();
    }

    @Test
    public void TextBlockController_getTextBlockResponseById_ReturnsTextBlockResponse() throws Exception {
        DataResult<TextBlockResponse> response = new SuccessDataResult<>(
                textBlockResponse, "TextBlock fetched!");

        Mockito.when(textBlockService.getTextBlockResponseById(Mockito.anyInt())).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{block-id}", 1)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("TextBlock fetched!"),
                jsonPath("$.data").exists(),
                jsonPath("$.data.id").value(textBlockResponse.getId())
        );
    }

    @Test
    public void TextBlockController_editTextBlock_ReturnsSuccessResult() throws Exception {
        TextBlockEditRequest request = TextBlockEditRequest.builder()
                .rawText(textBlockResponse.getRawText())
                .meaningfulText(textBlockResponse.getMeaningfulText())
                .mdText(textBlockResponse.getMdText())
                .build();
        Result result = new SuccessResult("TextBlock saved");

        Mockito.when(textBlockService.editTextBlock(Mockito.anyInt(), Mockito.any(TextBlockEditRequest.class))).thenReturn(result);

        mockMvc.perform(put(baseUri + "/{block-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.success").value(true),
                        jsonPath("$.message").value("TextBlock saved")
                );
    }

    @Test
    public void TextBlockController_deleteTextBlock_ReturnsSuccessResult() throws Exception {
        Result result = new SuccessResult("TextBlock deleted!");

        Mockito.when(textBlockService.deleteTextBlockById(Mockito.anyInt())).thenReturn(result);

        mockMvc.perform(delete(baseUri + "/{block-id}", 1)).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(true),
                jsonPath("$.message").value("TextBlock deleted!")
        );
    }

    @Test
    public void TextBlockController_getTextBlockResponseById_ReturnsBadRequest() throws Exception {
        int invalidBlockId = 999999;
        DataResult<TextBlockResponse> response = new ErrorDataResult<>("TextBlock not found by id: " + invalidBlockId);

        Mockito.when(textBlockService.getTextBlockResponseById(invalidBlockId)).thenReturn(response);

        mockMvc.perform(get(baseUri + "/{block-id}", invalidBlockId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("TextBlock not found by id: " + invalidBlockId)
        );
    }

    @Test
    public void TextBlockController_editTextBlock_ReturnsBadRequest_ForInvalidInput() throws Exception {
        TextBlockEditRequest invalidRequest = new TextBlockEditRequest();
        Result result = new ErrorResult("Invalid text block data");

        Mockito.when(textBlockService.editTextBlock(Mockito.anyInt(), Mockito.any(TextBlockEditRequest.class))).thenReturn(result);

        mockMvc.perform(put(baseUri + "/{block-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.success").value(false),
                        jsonPath("$.message").value("Invalid text block data")
                );
    }

    @Test
    public void TextBlockController_deleteTextBlock_ReturnsBadRequest_ForInvalidBlockId() throws Exception {
        int invalidBlockId = 999999;
        Result result = new ErrorResult("TextBlock not found by id: " + invalidBlockId);

        Mockito.when(textBlockService.deleteTextBlockById(invalidBlockId)).thenReturn(result);

        mockMvc.perform(delete(baseUri + "/{block-id}", invalidBlockId)).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.success").value(false),
                jsonPath("$.message").value("TextBlock not found by id: " + invalidBlockId)
        );
    }
}
