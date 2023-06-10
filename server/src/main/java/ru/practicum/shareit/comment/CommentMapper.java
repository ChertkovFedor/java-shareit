package ru.practicum.shareit.comment;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class CommentMapper {

    public static Comment mapToModel(Integer id, CommentCreateDto commentCreateDto, Item item, User author, Instant created) {
        return new Comment(
                id,
                commentCreateDto.getText(),
                item,
                author,
                created
        );
    }

    public static CommentDto mapToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentDto> mapToListDto(List<Comment> comments) {
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment c : comments) {
            commentDto.add(mapToDto(c));
        }
        return commentDto;
    }

}
