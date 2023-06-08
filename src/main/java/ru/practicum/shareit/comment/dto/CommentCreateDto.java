package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
    @Size(max = 255)
    private String text;
}