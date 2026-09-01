package com.gdgku.study.backend.game;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameResponse> create(
            @Valid @RequestBody GameCreateRequest request
    ) {
        GameResponse response = gameService.create(request);

        URI location = URI.create("/games/" + response.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> findAll(
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Difficulty difficulty
    ) {
        List<GameResponse> response =
                gameService.findAll(genre, difficulty);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> findById(
            @PathVariable Long gameId
    ) {
        GameResponse response = gameService.findById(gameId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long gameId
    ) {
        gameService.delete(gameId);

        return ResponseEntity.noContent().build();
    }
}