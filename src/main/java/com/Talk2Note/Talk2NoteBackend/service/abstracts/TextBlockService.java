package com.Talk2Note.Talk2NoteBackend.service.abstracts;

import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockEditRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;

import java.util.List;

public interface TextBlockService {

    DataResult<TextBlock> getTextBlockById(int textBlockId);
    DataResult<TextBlockResponse> getTextBlockResponseById(int textBlockId);

    DataResult<List<TextBlockResponse>> getAllTextBlocks();

    Result editTextBlock(int textBlockId, TextBlockEditRequest request);

    Result deleteTextBlockById(int textBlockId);

    Result saveTextBlock(TextBlock textBlock);

}
