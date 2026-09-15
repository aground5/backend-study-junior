package com.gdgku.study.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Gamecontroller {

    private final List<Game> games = new ArrayList<>();

    public Gamecontroller() {
        games.add(new Game(1L, "Minecraft", "Sandbox"));
        games.add(new Game(2L, "Elden Ring", "RPG"));
        games.add(new Game(3L, "Overwatch", "FPS"));
    }

    @GetMapping("/games")
    public List<Game> getGames(
            @RequestParam(required = false) String genre) {

        if (genre == null) {
            return games;
        }

        return games.stream()
                .filter(game -> game.getGenre().equalsIgnoreCase(genre))
                .toList();
}

    @GetMapping("/games/{id}")
    public Game getGame(@PathVariable Long id) {

        for (Game game : games) {
            if (game.getId().equals(id)) {
                return game;
        }
    }

    return null;
}
    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@RequestBody Game game) {

        games.add(game);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(game);
}

    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {

        games.removeIf(game -> game.getId().equals(id));

        return ResponseEntity.noContent().build();
}
}