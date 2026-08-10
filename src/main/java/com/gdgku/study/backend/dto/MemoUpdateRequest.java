package com.gdgku.study.backend.dto;

public class MemoUpdateRequest {
    private String title;
    private String content;

    public MemoUpdateRequest() {}

    public MemoUpdateRequest(String title, String content) {
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
