package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.Instant;

@Data
@AllArgsConstructor
public class CommentDto {
    private Integer id;
    private String text;
    private String authorName;
    private Instant created;
}