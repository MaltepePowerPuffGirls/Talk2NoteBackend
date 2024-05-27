package com.Talk2Note.Talk2NoteBackend.service.concretes;

import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockEditRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.*;
import com.Talk2Note.Talk2NoteBackend.core.utils.AuthUserUtil;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;
import com.Talk2Note.Talk2NoteBackend.entity.User;
import com.Talk2Note.Talk2NoteBackend.repository.TextBlockRepository;
import com.Talk2Note.Talk2NoteBackend.service.abstracts.TextBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.Talk2Note.Talk2NoteBackend.core.utils.DtoMapUtil.generateTextBlockResponse;
import static com.Talk2Note.Talk2NoteBackend.core.utils.DtoMapUtil.generateTextBlockResponses;

@Service
@RequiredArgsConstructor
public class TextBlockManager implements TextBlockService {
    private final TextBlockRepository textBlockRepository;
    private final AuthUserUtil authUserUtil;

    @Override
    public DataResult<TextBlock> getTextBlockById(int textBlockId) {
        TextBlock textBlock = textBlockRepository.findById(textBlockId).orElse(null);
        if(textBlock == null){
            return new ErrorDataResult<>("TextBlock not found by id: " + textBlockId);
        }
        return new SuccessDataResult<>(textBlock,"TextBlock found!");
    }

    @Override
    public DataResult<TextBlockResponse> getTextBlockResponseById(int textBlockId) {
        DataResult<TextBlock> textBlockResult = getTextBlockById(textBlockId);
        if(!textBlockResult.isSuccess()){
            return new ErrorDataResult<>(textBlockResult.getMessage());
        }
        TextBlock textBlock = (TextBlock) textBlockResult.getData();
        TextBlockResponse response = generateTextBlockResponse(textBlock);

        return new SuccessDataResult<>(response,"TextBlock fetched!");
    }

    @Override
    public DataResult<List<TextBlockResponse>> getAllTextBlocks() {
        List<TextBlock> textBlocks = textBlockRepository.findAll();

        List<TextBlockResponse> textBlockResponses = generateTextBlockResponses(textBlocks);

        return new SuccessDataResult<>(textBlockResponses,"All textblocks fetched!");
    }

    @Override
    public Result editTextBlock(int textBlockId, TextBlockEditRequest request) {
        DataResult<TextBlock> textBlocResult = getTextBlockById(textBlockId);
        if (!textBlocResult.isSuccess()){
            return new ErrorResult(textBlocResult.getMessage());
        }

        TextBlock textBlock = (TextBlock) textBlocResult.getData();

        textBlock.setModified(true);
        textBlock.setRawText(request.getRawText());
        textBlock.setMeaningfulText(request.getMeaningfulText());

        return save(textBlock);
    }

    @Override
    public Result deleteTextBlockById(int textBlockId) {
        User authUser = authUserUtil.getAuthenticatedUser();
        if(authUser == null){
            return new ErrorResult("User not authenticated!");
        }

        DataResult<TextBlock> textBlockResult = getTextBlockById(textBlockId);
        if(!textBlockResult.isSuccess()){
            return new ErrorResult(textBlockResult.getMessage());
        }
        TextBlock textBlock = (TextBlock) textBlockResult.getData();

        if(textBlock.getNote().getAuthor() != authUser){
            return new ErrorResult("User not authorized for textblock deletion!");
        }
        return deleteTextBlock(textBlock);
    }

    @Override
    public DataResult<List<TextBlock>> getAllTextBlockByNoteAscRow(Note note) {
        List<TextBlock> textBlocks = textBlockRepository.getAllByNoteOrderByRowNumberAsc(note);
        return new SuccessDataResult<>(textBlocks, "TextBlock found by Note ORDERED by RowNumber ASC");
    }

    @Override
    public Result save(TextBlock textBlock) {
        try {
            textBlockRepository.save(textBlock);
        } catch (Exception e) {
            return new ErrorResult("UEO: " + e.getMessage());
        }
        return new SuccessResult("TextBlock saved");
    }

    private Result deleteTextBlock(TextBlock textBlock) {
        try{
            textBlockRepository.delete(textBlock);
        }catch (Exception e){
            return new ErrorResult(e.getMessage());
        }
        return new SuccessResult("TextBlock deleted!");
    }

}
