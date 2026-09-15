package com.gdgku.study.backend;

public class Game {

    private Long id;
    private String title;
    private String genre;

    public Game() {
    }

    public Game(Long id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }
}