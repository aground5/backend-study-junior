package com.gdgku.study.backend;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String genre;

    public Book(Long id, String title, String author, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    // Getter & Setter (스프링이 JSON으로 변환할 때 필수)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
}