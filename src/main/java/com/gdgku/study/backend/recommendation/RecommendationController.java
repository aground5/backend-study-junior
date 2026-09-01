package com.gdgku.study.backend.recommendation;

import java.util.List;

import com.gdgku.study.backend.game.Difficulty;
import com.gdgku.study.backend.game.GameResponse;
import com.gdgku.study.backend.game.GameService;
import com.gdgku.study.backend.game.Genre;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final GameService gameService;

    public RecommendationController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> recommend(
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Difficulty difficulty
    ) {
        List<GameResponse> response =
                gameService.recommend(genre, difficulty);

        return ResponseEntity.ok(response);
    }
}