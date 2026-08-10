package com.gdgku.study.backend.controller;

import com.gdgku.study.backend.model.Memo;
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
    public Memo createMemo(@RequestBody Memo memo) {
        return memoService.createMemo(memo);
    }

    @GetMapping
    public List<Memo> getAllMemos() {
        return memoService.getAllMemos();
    }

    @GetMapping("/{id}")
    public Memo getMemoById(@PathVariable Long id) {
        return memoService.getMemoById(id);
    }

    @PutMapping("/{id}")
    public Memo updateMemo(@PathVariable Long id, @RequestBody Memo request) {
        return memoService.updateMemo(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMemo(@PathVariable Long id) {
        memoService.deleteMemo(id);
    }
}
