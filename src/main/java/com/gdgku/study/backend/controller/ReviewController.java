package com.gdgku.study.backend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final List<Review> reviewList = new ArrayList<>();
    private long nextId = 1L;

    public static class Review {
        private Long id;
        private String artist;
        private String title;
        private Integer rating;
        private String content;

        public Review() {}
        public Review(Long id, String artist, String title, Integer rating, String content) {
            this.id = id;
            this.artist = artist;
            this.title = title;
            this.rating = rating;
            this.content = content;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getArtist() { return artist; }
        public void setArtist(String artist) { this.artist = artist; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    // 롤링 스톤 선정 500대 명반 중 1~30위!
    public ReviewController() {
        reviewList.add(new Review(nextId++, "Marvin Gaye", "What's Going On", 5, ""));
        reviewList.add(new Review(nextId++, "The Beach Boys", "Pet Sounds", 5, ""));
        reviewList.add(new Review(nextId++, "Joni Mitchell", "Blue", 5, ""));
        reviewList.add(new Review(nextId++, "Stevie Wonder", "Songs in the Key of Life", 5, ""));
        reviewList.add(new Review(nextId++, "The Beatles", "Abbey Road", 5, ""));
        reviewList.add(new Review(nextId++, "Nirvana", "Nevermind", 5, ""));
        reviewList.add(new Review(nextId++, "Fleetwood Mac", "Rumours", 5, ""));
        reviewList.add(new Review(nextId++, "Prince", "Purple Rain", 5, ""));
        reviewList.add(new Review(nextId++, "Bob Dylan", "Blood on the Tracks", 5, ""));
        reviewList.add(new Review(nextId++, "Lauryn Hill", "The Miseducation of Lauryn Hill", 5, ""));
        reviewList.add(new Review(nextId++, "Michael Jackson", "Thriller", 5, ""));
        reviewList.add(new Review(nextId++, "The Beatles", "Revolver", 5, ""));
        reviewList.add(new Review(nextId++, "Kanye West", "My Beautiful Dark Twisted Fantasy", 5, ""));
        reviewList.add(new Review(nextId++, "The Rolling Stones", "Exile on Main St.", 5, ""));
        reviewList.add(new Review(nextId++, "The Clash", "London Calling", 5, ""));
        reviewList.add(new Review(nextId++, "Bob Dylan", "Highway 61 Revisited", 5, ""));
        reviewList.add(new Review(nextId++, "Amy Winehouse", "Back to Black", 5, ""));
        reviewList.add(new Review(nextId++, "The Beatles", "The White Album", 5, ""));
        reviewList.add(new Review(nextId++, "James Brown", "Live at the Apollo", 5, ""));
        reviewList.add(new Review(nextId++, "The Miles Davis Quintet", "Kind of Blue", 5, ""));
        reviewList.add(new Review(nextId++, "OutKast", "Aquemini", 5, ""));
        reviewList.add(new Review(nextId++, "Aretha Franklin", "I Never Loved a Man the Way I Love You", 5, ""));
        reviewList.add(new Review(nextId++, "Prince and the Revolution", "Sign O' the Times", 5, ""));
        reviewList.add(new Review(nextId++, "Bob Marley and the Wailers", "Legend", 5, ""));
        reviewList.add(new Review(nextId++, "Public Enemy", "It Takes a Nation of Millions to Hold Us Back", 5, ""));
        reviewList.add(new Review(nextId++, "D'Angelo and the Vanguard", "Black Messiah", 5, ""));
        reviewList.add(new Review(nextId++, "The Velvet Underground and Nico", "The Velvet Underground and Nico", 5, ""));
        reviewList.add(new Review(nextId++, "Radiohead", "Kid A", 5, ""));
        reviewList.add(new Review(nextId++, "Elton John", "Goodbye Yellow Brick Road", 5, ""));
        reviewList.add(new Review(nextId++, "Beyoncé", "Lemonade", 5, ""));
    }

    @PostMapping
    public Review createReview(
            @RequestParam String artist,
            @RequestParam String title,
            @RequestParam Integer rating,
            @RequestParam String content) {

    Review review = new Review(nextId++, artist, title, rating, content);
    reviewList.add(review);
    return review;
    }
    
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewList;
    }

    @GetMapping("/high-rating")
    public List<Review> getHighRatedReviews() {
        List<Review> result = new ArrayList<>();
        for (Review r : reviewList) {
            if (r.getRating() >= 3) {
                result.add(r);
            }
        }
        return result;
    }
}
