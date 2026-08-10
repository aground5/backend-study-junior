package com.gdgku.study.backend.service;

import com.gdgku.study.backend.model.Memo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemoService {

    private final List<Memo> memoList = new ArrayList<>();
    private long nextId = 1L;

    public Memo createMemo(Memo memo) {
        memo.setId(nextId++);
        memoList.add(memo);
        return memo;
    }

    public List<Memo> getAllMemos() {
        return memoList;
    }

    public Memo getMemoById(Long id) {
        for (Memo m : memoList) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public Memo updateMemo(Long id, Memo request) {
        for (Memo m : memoList) {
            if (m.getId().equals(id)) {
                m.setTitle(request.getTitle());
                m.setContent(request.getContent());
                return m;
            }
        }
        return null;
    }

    public void deleteMemo(Long id) {
        memoList.removeIf(m -> m.getId().equals(id));
    }
}
