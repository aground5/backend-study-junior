package com.gdgku.study.backend.memo;

import com.gdgku.study.backend.memo.dto.MemoCreateRequest;
import com.gdgku.study.backend.memo.dto.MemoResponse;
import com.gdgku.study.backend.memo.dto.MemoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    @Transactional
    public MemoResponse createMemo(MemoCreateRequest request) {
        Memo memo = new Memo(request.getTitle(), request.getContent());
        Memo savedMemo = memoRepository.save(memo);
        return MemoResponse.from(savedMemo);
    }

    public List<MemoResponse> getAllMemos() {
        return memoRepository.findAll().stream()
                .map(MemoResponse::from)
                .collect(Collectors.toList());
    }

    public MemoResponse getMemoById(Long id) {
        Memo memo = memoRepository.findById(id).orElse(null);
        return memo != null ? MemoResponse.from(memo) : null;
    }

    @Transactional
    public MemoResponse updateMemo(Long id, MemoUpdateRequest request) {
        Memo memo = memoRepository.findById(id).orElse(null);
        if (memo != null) {
            memo.update(request.getTitle(), request.getContent());
            Memo updatedMemo = memoRepository.save(memo);
            return MemoResponse.from(updatedMemo);
        }
        return null;
    }

    @Transactional
    public void deleteMemo(Long id) {
        memoRepository.deleteById(id);
    }
}
