package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllCommentsByItemIdIn(List<Integer> itemId);

    CommentDto create(Comment comment);

}
