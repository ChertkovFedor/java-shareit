package ru.practicum.shareit.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository cRep;

    @Override
    public List<Comment> getAllCommentsByItemIdIn(List<Integer> itemId) {
        return cRep.findAllByItemIdIn(itemId);
    }

    @Override
    public CommentDto create(Comment comment) {
        return CommentMapper.mapToDto(cRep.save(comment));
    }

}
