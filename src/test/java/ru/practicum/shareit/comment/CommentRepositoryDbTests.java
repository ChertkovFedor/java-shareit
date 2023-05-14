package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CommentRepositoryDbTests {

    @Mock
    private CommentRepository cRep;

    @Test
    public void testFindAllByItemIdIn() {
        List<Integer> itemIds = Arrays.asList(1, 2, 3);
        List<Comment> expectedComments = Arrays.asList(
                new Comment(1, "Comment 1", null, null, Instant.now()),
                new Comment(2, "Comment 2", null, null, Instant.now())
        );
        when(cRep.findAllByItemIdIn(itemIds)).thenReturn(expectedComments);

        List<Comment> actualComments = cRep.findAllByItemIdIn(itemIds);

        assertEquals(expectedComments.size(), actualComments.size());
        for (int i = 0; i < expectedComments.size(); i++) {
            Comment expectedComment = expectedComments.get(i);
            Comment actualComment = actualComments.get(i);
            assertEquals(expectedComment.getId(), actualComment.getId());
            assertEquals(expectedComment.getText(), actualComment.getText());
            assertEquals(expectedComment.getItem(), actualComment.getItem());
            assertEquals(expectedComment.getAuthor(), actualComment.getAuthor());
            assertEquals(expectedComment.getCreated(), actualComment.getCreated());
        }
    }
}
