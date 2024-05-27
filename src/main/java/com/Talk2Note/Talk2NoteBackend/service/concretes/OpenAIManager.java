package com.Talk2Note.Talk2NoteBackend.service.concretes;

import com.Talk2Note.Talk2NoteBackend.core.enums.OpenAIRoleType;
import com.Talk2Note.Talk2NoteBackend.core.proxy.OpenAIConnector;
import com.Talk2Note.Talk2NoteBackend.core.results.*;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.Talk2Note.Talk2NoteBackend.repository.NoteRepository;
import com.Talk2Note.Talk2NoteBackend.repository.TextBlockRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.OpenAIService;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.TextBlockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAIManager implements OpenAIService {

    private final OpenAIConnector openAIConnector;
    private final TextBlockService textBlockService;
    private final NoteRepository noteRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Result rawToMeaningful(Note note) {

        DataResult<Map<Integer, String>> mappedNoteResult = getRawNoteRowTextMap(note);
        if (!mappedNoteResult.isSuccess()) {
            return new ErrorResult(mappedNoteResult.getMessage());
        }

        String jsonStringDataResult = mapToJson(mappedNoteResult.getData());
        String rawResponse;

        try {
            rawResponse = openAIConnector.compose(jsonStringDataResult, OpenAIRoleType.RAW_TO_MEANINGFUL);
        } catch (Exception e) {
            return new ErrorResult("UEO: " + e);
        }

        if (rawResponse == null) {
            return new ErrorResult("Error occurred white converting Raw to Meaningful");
        }

        Map responseMap = jsonToMap(rawResponse);
        if (responseMap == null) {
            return new ErrorResult("response map null");
        }

        for (TextBlock block: note.getTextBlocks()) {
            block.setMeaningfulText((String) responseMap.get(String.valueOf(block.getRowNumber())));
            textBlockService.save(block);
        }

        return new SuccessResult("Setting Meaningful text completed");

    }

    @Override
    public Result MdAuto(Note note) {

        DataResult<Map<Integer, String>> mappedNoteResult = getMeaningfulNoteRowTextMap(note);
        if (!mappedNoteResult.isSuccess()) {
            return new ErrorResult(mappedNoteResult.getMessage());
        }

        String totalParagraph = String.join("\n", mappedNoteResult.getData().values());

        String rawResponse;
        try {
            rawResponse = openAIConnector.compose(totalParagraph, OpenAIRoleType.MD_AUTO);
        } catch (Exception e) {
            return new ErrorResult("UEO: " + e);
        }

        if(rawResponse == null){
            return new ErrorResult("Error occurred while converting text to md");
        }

        note.setMarkdownText(rawResponse);
        noteRepository.save(note);

        return new SuccessResult("Setting Meaningful text completed");
    }

    private DataResult<Map<Integer, String>> getRawNoteRowTextMap(Note note) {
        DataResult<List<TextBlock>> textBlockResult = textBlockService.getAllTextBlockByNoteAscRow(note);
        if (!textBlockResult.isSuccess()) {
            return new ErrorDataResult<>(textBlockResult.getMessage());
        }
        List<TextBlock> textBlocks = textBlockResult.getData();

        Map<Integer, String> rowNumberToRawTextMap = textBlocks.stream()
                .collect(Collectors.toMap(TextBlock::getRowNumber, TextBlock::getRawText));
        return new SuccessDataResult(rowNumberToRawTextMap, "Note mapped");
    }

    private DataResult<Map<Integer, String>> getMeaningfulNoteRowTextMap(Note note) {
        DataResult<List<TextBlock>> textBlockResult = textBlockService.getAllTextBlockByNoteAscRow(note);
        if (!textBlockResult.isSuccess()) {
            return new ErrorDataResult<>(textBlockResult.getMessage());
        }
        List<TextBlock> textBlocks = textBlockResult.getData();

        Map<Integer, String> rowNumberToRawTextMap = textBlocks.stream()
                .collect(Collectors.toMap(TextBlock::getRowNumber, TextBlock::getMeaningfulText));
        return new SuccessDataResult(rowNumberToRawTextMap, "Note mapped");
    }

    private String mapToJson(Map<Integer, String> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<Integer, String> jsonToMap(String jsonString) {
        try {
            return mapper.readValue(jsonString, Map.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    boolean allConditionsMet = textBlocks.size() == response.size()
                && textBlocks.stream().allMatch(tb -> response.containsKey(tb.getRowNumber()));
     */

}
