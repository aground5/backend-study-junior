package com.gdgku.study.backend.game;

public record GameResponse(
        Long id,
        String title,
        Genre genre,
        Difficulty difficulty
) {

    public static GameResponse from(Game game) {
        return new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getGenre(),
                game.getDifficulty()
        );
    }
}