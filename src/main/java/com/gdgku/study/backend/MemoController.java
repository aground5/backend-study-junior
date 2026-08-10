package com.gdgku.study.backend;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private final List<Memo> memoList = new ArrayList<>();
    private long nextId = 1L;

    public static class Memo {
        private Long id;
        private String title;
        private String content;

        public Memo() {}
        public Memo(Long id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    @PostMapping
    public Memo createMemo(@RequestBody Memo memo) {
        memo.setId(nextId++);
        memoList.add(memo);
        return memo;
    }

    @GetMapping
    public List<Memo> getAllMemos() {
        return memoList;
    }

    @GetMapping("/{id}")
    public Memo getMemoById(@PathVariable Long id) {
        for (Memo m : memoList) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    @PutMapping("/{id}")
    public Memo updateMemo(@PathVariable Long id, @RequestBody Memo request) {
        for (Memo m : memoList) {
            if (m.getId().equals(id)) {
                m.setTitle(request.getTitle());
                m.setContent(request.getContent());
                return m;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteMemo(@PathVariable Long id) {
        memoList.removeIf(m -> m.getId().equals(id));
    }
}
