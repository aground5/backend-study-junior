package com.gdgku.study.backend.game;

import java.util.Comparator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameResponse create(GameCreateRequest request) {
        Game game = new Game(
                request.title(),
                request.genre(),
                request.difficulty()
        );

        Game savedGame = gameRepository.save(game);

        return GameResponse.from(savedGame);
    }

    @Transactional(readOnly = true)
    public List<GameResponse> findAll(
            Genre genre,
            Difficulty difficulty
    ) {
        return gameRepository.findAll()
                .stream()
                .filter(game ->
                        genre == null || game.getGenre() == genre
                )
                .filter(game ->
                        difficulty == null
                                || game.getDifficulty() == difficulty
                )
                .sorted(Comparator.comparing(Game::getId))
                .map(GameResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GameResponse findById(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "게임을 찾을 수 없습니다."
                ));

        return GameResponse.from(game);
    }

    @Transactional(readOnly = true)
    public List<GameResponse> recommend(
            Genre genre,
            Difficulty difficulty
    ) {
        return findAll(genre, difficulty)
                .stream()
                .limit(3)
                .toList();
    }

    public void delete(Long gameId) {
        if (!gameRepository.existsById(gameId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "게임을 찾을 수 없습니다."
            );
        }

        gameRepository.deleteById(gameId);
    }
}