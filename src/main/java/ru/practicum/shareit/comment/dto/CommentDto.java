package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@AllArgsConstructor
public class CommentDto {
    private Integer id;
    @NotBlank
    @Size(max = 255)
    private String text;
    @NotBlank
    private String authorName;
    @Past
    private Instant created;
}