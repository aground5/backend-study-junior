package com.gdgku.study.backend.controller;

import com.gdgku.study.backend.dto.MemoCreateRequest;
import com.gdgku.study.backend.dto.MemoResponse;
import com.gdgku.study.backend.dto.MemoUpdateRequest;
import com.gdgku.study.backend.service.MemoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @PostMapping
    public MemoResponse createMemo(@RequestBody MemoCreateRequest request) {
        return memoService.createMemo(request);
    }

    @GetMapping
    public List<MemoResponse> getAllMemos() {
        return memoService.getAllMemos();
    }

    @GetMapping("/{id}")
    public MemoResponse getMemoById(@PathVariable Long id) {
        return memoService.getMemoById(id);
    }

    @PutMapping("/{id}")
    public MemoResponse updateMemo(@PathVariable Long id, @RequestBody MemoUpdateRequest request) {
        return memoService.updateMemo(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMemo(@PathVariable Long id) {
        memoService.deleteMemo(id);
    }
}
