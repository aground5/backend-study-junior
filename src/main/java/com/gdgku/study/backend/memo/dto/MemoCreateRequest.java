package com.gdgku.study.backend.memo.dto;

public class MemoCreateRequest {
    private String title;
    private String content;

    public MemoCreateRequest() {}

    public MemoCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
