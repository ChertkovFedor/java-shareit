package ru.practicum.shareit.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommentServiceTests {

    @Mock
    private CommentRepository cRep;

    @InjectMocks
    private CommentServiceImpl cServ;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCommentsByItemIdIn() {
        List<Integer> itemIds = Arrays.asList(1, 2, 3);
        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment(1, "Comment 1", null, null, Instant.now()));
        expectedComments.add(new Comment(2, "Comment 2", null, null, Instant.now()));
        when(cRep.findAllByItemIdIn(itemIds)).thenReturn(expectedComments);

        List<Comment> actualComments = cServ.getAllCommentsByItemIdIn(itemIds);

        assertEquals(expectedComments, actualComments);
        verify(cRep, times(1)).findAllByItemIdIn(itemIds);
    }

    @Test
    public void testCreate() {
        User user = new User(1, "name", "name@mail.com");
        Comment comment = new Comment(1, "Comment", null, user, Instant.now());
        when(cRep.save(comment)).thenReturn(comment);

        CommentDto createdCommentDto = cServ.create(comment);

        CommentDto expectedCommentDto = new CommentDto(
                comment.getId(),
                comment.getText(),
                "name",
                comment.getCreated()
        );
        assertEquals(expectedCommentDto, createdCommentDto);
        verify(cRep, times(1)).save(comment);
    }
}

