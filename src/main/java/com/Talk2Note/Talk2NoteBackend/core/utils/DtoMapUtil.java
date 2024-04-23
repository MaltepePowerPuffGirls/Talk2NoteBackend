package com.Talk2Note.Talk2NoteBackend.core.utils;

import com.Talk2Note.Talk2NoteBackend.api.dto.MemberResponse;
import com.Talk2Note.Talk2NoteBackend.api.dto.NoteResponse;
import com.Talk2Note.Talk2NoteBackend.api.dto.TextBlockResponse;
import com.Talk2Note.Talk2NoteBackend.entity.Member;
import com.Talk2Note.Talk2NoteBackend.entity.Note;
import com.Talk2Note.Talk2NoteBackend.entity.TextBlock;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapUtil {

    public static MemberResponse generateMemberResponse(Member member){
        return MemberResponse.builder()
                .id(member.getId())
                .authority(member.getAuthority())
                .accepted(member.isAccepted())
                .noteId(member.getNote().getId())
                .userId(member.getUser().getId())
                .invitedAt(member.getInvitedAt())
                .acceptedAt(member.getAcceptedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }

    public static TextBlockResponse generateTextBlockResponse(TextBlock textBlock){
        return TextBlockResponse.builder()
                .id(textBlock.getId())
                .rowNumber(textBlock.getRowNumber())
                .modified(textBlock.isModified())
                .rawText(textBlock.getRawText())
                .meaningfulText(textBlock.getMeaningfulText())
                .mdText(textBlock.getMdText())
                .noteId(textBlock.getNote().getId())
                .createdAt(textBlock.getCreatedAt())
                .modifiedAt(textBlock.getModifiedAt())
                .build();
    }

    public static NoteResponse generateNoteResponse(Note note){
        return NoteResponse.builder()
                .id(note.getId())
                .noteName(note.getNoteName())
                .priority(note.getPriority())
                .noteStatus(note.getNoteStatus())
                .pinned(note.isPinned())
                .description(note.getDescription())
                .authorId(note.getAuthor().getId())
                .textBlocks(generateTextBlockResponses(note.getTextBlocks()))
                .members(generateMemberResponses(note.getMembers()))
                .createdAt(note.getCreatedAt())
                .modifiedAt(note.getModifiedAt())
                .build();
    }

    public static List<MemberResponse> generateMemberResponses(List<Member> members){
        return members.stream()
                .map(DtoMapUtil::generateMemberResponse)
                .collect(Collectors.toList());
    }

    public static List<TextBlockResponse> generateTextBlockResponses(List<TextBlock> textBlocks){
        return textBlocks.stream()
                .map(DtoMapUtil::generateTextBlockResponse)
                .collect(Collectors.toList());
    }

    public static List<NoteResponse> generateNoteResponses(List<Note> notes){
        return notes.stream()
                .map(DtoMapUtil::generateNoteResponse)
                .collect(Collectors.toList());
    }
}
