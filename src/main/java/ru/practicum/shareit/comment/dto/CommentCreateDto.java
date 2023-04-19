package ru.practicum.shareit.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CommentCreateDto {
    @Size(max = 255)
    private String text;
}