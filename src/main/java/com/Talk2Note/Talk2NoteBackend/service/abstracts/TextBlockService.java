package com.Talk2Note.Talk2NoteBackend.service.abstracts;

import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockCreateRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockEditRequest;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
import com.Talk2Note.Talk2NoteBackend.core.results.DataResult;
import com.Talk2Note.Talk2NoteBackend.core.results.Result;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;

import java.util.List;

public interface TextBlockService {

    DataResult<TextBlockResponse> getTextBlockById(int textBlockId);

    DataResult<List<TextBlockResponse>> getAllTextBlocks();

    Result editTextBlock(int textBlockId, TextBlockEditRequest request);

    Result deleteTextBlockById(int textBlockId);

    private Result saveTextBlock(TextBlock textBlock){return null;}

    private Result deleteTextBlock(TextBlock textBlock){return null;}

    private DataResult<TextBlock> getTextBlock(int textBlockId){return null;}
}
