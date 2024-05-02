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

        Map<Integer, String> response = openAIConnector.compose(rowNumberToRawTextMap, OpenAIRoleType.RAW_TO_MEANINGFUL);
        if (response == null) {
            return new ErrorResult("Error occurred white converting Raw to Meaningful");
        }

        boolean allConditionsMet = textBlocks.size() == response.size()
                && textBlocks.stream().allMatch(tb -> response.containsKey(tb.getRowNumber()));
        if (!allConditionsMet) {
            return new ErrorResult("Generated Response not matching error");
        }

        return new SuccessResult("Temporary Result Displayer : " + String.valueOf(response));
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

        Map<Integer,String> response = openAIConnector.compose(rowNumberToAutoTextMap,OpenAIRoleType.MD_AUTO);
        if(response == null){
            return new ErrorResult("Error occurred while converting text to md");
        }

        boolean allConditionsMet = textBlocks.size() == response.size()
                && textBlocks.stream().allMatch(tb -> response.containsKey(tb.getRowNumber()));
        if (!allConditionsMet) {
            return new ErrorResult("Generated Response Error");
        }

        return new SuccessResult("Temporary result Displayer : " + String.valueOf(response));
        /*
        for (TextBlock block: textBlocks) {
            block.setMdText(response.get(block.getRowNumber()));
            textBlockService.save(block);
        */

        // return new SuccessResult("Setting Meaningful text completed");
    }


}
