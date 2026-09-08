package com.gdgku.study.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/legacy-books") // com.gdgku.library.LibraryController와 경로가 겹쳐 /books에서 변경
public class BookController {

    // DB 대신 사용할 임시 메모리 저장소
    private final Map<Long, Book> bookStorage = new HashMap<>();
    private long sequence = 1L;

    // 1. 도서 생성 (201 Created 반환)
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book newBook = new Book(sequence, book.getTitle(), book.getAuthor(), book.getGenre());
        bookStorage.put(sequence, newBook);
        sequence++;
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
    }

    // 2. 도서 목록 조회 및 장르별 필터링 (Query Parameter 사용, 200 OK)
    // 예: GET /books 또는 GET /books?genre=IT
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String genre) {
        if (genre != null) {
            List<Book> filtered = bookStorage.values().stream()
                    .filter(b -> b.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filtered);
        }
        return ResponseEntity.ok(new ArrayList<>(bookStorage.values()));
    }

    // 3. 특정 ID 도서 단건 조회 (Path Variable 사용, 200 OK 또는 404 NOT_FOUND)
    // 예: GET /books/1
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookStorage.get(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(book);
    }

    // 4. 특정 ID 도서 삭제 (Path Variable 사용, 204 No Content)
    // 예: DELETE /books/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookStorage.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        bookStorage.remove(id);
        return ResponseEntity.noContent().build(); // 204 상태 코드 반환
    }
}