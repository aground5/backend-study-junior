package com.gdgku.study.backend.service;

import com.gdgku.study.backend.dto.MemoCreateRequest;
import com.gdgku.study.backend.dto.MemoResponse;
import com.gdgku.study.backend.dto.MemoUpdateRequest;
import com.gdgku.study.backend.model.Memo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoService {

    private final List<Memo> memoList = new ArrayList<>();
    private long nextId = 1L;

    public MemoResponse createMemo(MemoCreateRequest request) {
        Memo memo = new Memo(nextId++, request.getTitle(), request.getContent());
        memoList.add(memo);
        return MemoResponse.from(memo);
    }

    public List<MemoResponse> getAllMemos() {
        return memoList.stream()
                .map(MemoResponse::from)
                .collect(Collectors.toList());
    }

    public MemoResponse getMemoById(Long id) {
        for (Memo m : memoList) {
            if (m.getId().equals(id)) {
                return MemoResponse.from(m);
            }
        }
        return null;
    }

    public MemoResponse updateMemo(Long id, MemoUpdateRequest request) {
        for (Memo m : memoList) {
            if (m.getId().equals(id)) {
                m.setTitle(request.getTitle());
                m.setContent(request.getContent());
                return MemoResponse.from(m);
            }
        }
        return null;
    }

    public void deleteMemo(Long id) {
        memoList.removeIf(m -> m.getId().equals(id));
    }
}
