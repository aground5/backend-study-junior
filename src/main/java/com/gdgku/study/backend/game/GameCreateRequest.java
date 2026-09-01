package com.gdgku.study.backend.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GameCreateRequest(

        @NotBlank
        @Size(max = 100)
        String title,

        @NotNull
        Genre genre,

        @NotNull
        Difficulty difficulty
) {
}