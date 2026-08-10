package com.gdgku.study.backend.dto;

import com.gdgku.study.backend.model.Memo;

public class MemoResponse {
    private Long id;
    private String title;
    private String content;

    public MemoResponse() {}

    public MemoResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static MemoResponse from(Memo memo) {
        return new MemoResponse(memo.getId(), memo.getTitle(), memo.getContent());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
