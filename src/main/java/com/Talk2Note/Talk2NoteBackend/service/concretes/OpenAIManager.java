package com.Talk2Note.Talk2NoteBackend.service.concretes;

import com.Talk2Note.Talk2NoteBackend.core.enums.OpenAIRoleType;
import com.Talk2Note.Talk2NoteBackend.core.proxy.OpenAIConnector;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.ErrorResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.core.results.SuccessResult;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.OpenAIService;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.TextBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAIManager implements OpenAIService {

    private final OpenAIConnector openAIConnector;
    private final TextBlockService textBlockService;

    @Override
    public Result rawToMeaningful(Note note) {

        DataResult<List<TextBlock>> textBlockResult = textBlockService.getAllTextBlockByNoteAscRow(note);
        if (!textBlockResult.isSuccess()) {
            return new ErrorResult(textBlockResult.getMessage());
        }
        List<TextBlock> textBlocks = textBlockResult.getData();

        Map<Integer, String> rowNumberToRawTextMap = textBlocks.stream()
                .collect(Collectors.toMap(TextBlock::getRowNumber, TextBlock::getRawText));

        String composeString = generatePrompt(rowNumberToRawTextMap);

        String rawResponse = openAIConnector.compose(composeString, OpenAIRoleType.RAW_TO_MEANINGFUL);
        if (rawResponse == null) {
            return new ErrorResult("Error occurred white converting Raw to Meaningful");
        }
        Map<Integer, String> response = convertGeneratedResponse(rawResponse);

        boolean allConditionsMet = textBlocks.size() == response.size()
                && textBlocks.stream().allMatch(tb -> response.containsKey(tb.getRowNumber()));
        if (!allConditionsMet) {
            return new ErrorResult("Generated Response not matching error");
        }

        return new SuccessResult("Temporary Result Displayer [need fix] : " + String.valueOf(response));
        /*
        for (TextBlock block: textBlocks) {
            block.setMeaningfulText(response.get(block.getRowNumber()));
            textBlockService.save(block);
        }
         */

        // return new SuccessResult("Setting Meaningful text completed");

    }

    @Override
    public Result MdAuto(Note note) {
        DataResult<List<TextBlock>> textBlockResult = textBlockService.getAllTextBlockByNoteAscRow(note);
        if (!textBlockResult.isSuccess()) {
            return new ErrorResult(textBlockResult.getMessage());
        }
        List<TextBlock> textBlocks = textBlockResult.getData();

        Map<Integer, String> rowNumberToAutoTextMap = textBlocks.stream()
                .collect(Collectors.toMap(TextBlock::getRowNumber, TextBlock::getMeaningfulText));

        String composeString = generatePrompt(rowNumberToAutoTextMap);

        String rawResponse = openAIConnector.compose(composeString,OpenAIRoleType.MD_AUTO);
        if(rawResponse == null){
            return new ErrorResult("Error occurred while converting text to md");
        }
        Map<Integer, String> response = convertGeneratedResponse(rawResponse);

        boolean allConditionsMet = textBlocks.size() == response.size()
                && textBlocks.stream().allMatch(tb -> response.containsKey(tb.getRowNumber()));
        if (!allConditionsMet) {
            return new ErrorResult("Generated Response Error");
        }

        return new SuccessResult("Temporary result [need fix] : " + String.valueOf(response));
        /*
        for (TextBlock block: textBlocks) {
            block.setMdText(response.get(block.getRowNumber()));
            textBlockService.save(block);
        */

        // return new SuccessResult("Setting Meaningful text completed");
    }

    @Override
    public String chatByRole(Map<Integer, String> chat, OpenAIRoleType type) {

        return openAIConnector.compose(generatePrompt(chat), type);

    }

    @Override
    public Map<Integer, String> chatByRoleParsed(Map<Integer, String> chat, OpenAIRoleType type) {

        String rawResponse = chatByRole(chat, type);
        return convertGeneratedResponse(rawResponse);

    }

    @Override
    public String generatePrompt(Map<Integer, String> content) {
        // string -> <<1::text1##2::text2..>>
        StringBuilder promptBuilder = new StringBuilder();
        for (Map.Entry<Integer, String> entry : content.entrySet()) {
            promptBuilder.append("<<").append(entry.getKey()).append("::").append(entry.getValue()).append(">>##");
        }
        // Remove the extra '##' at the end
        promptBuilder.delete(promptBuilder.length() - 2, promptBuilder.length());
        return promptBuilder.toString();
    }

    @Override
    public Map<Integer, String> convertGeneratedResponse(String responseText) {
        // <<1::text1##2::text2..>> -> string
        Map<Integer, String> responseMap = new HashMap<>();
        // Split the responseText by '##' to get individual prompts
        String[] prompts = responseText.split("##");
        for (String prompt : prompts) {
            // Split each prompt by '::' to separate index and text
            String[] parts = prompt.split("::");
            if (parts.length == 2) {
                int index = Integer.parseInt(parts[0].substring(2)); // Extract index from "<<index::text>>"
                String text = parts[1].substring(0, parts[1].length() - 2); // Remove ">>" from text
                responseMap.put(index, text);
            }
        }
        return responseMap;
    }

}
