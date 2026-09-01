package com.gdgku.study.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/holds")
public class HoldController {

    // DB 대신 사용할 메모리 데이터 저장소 (가짜 DB)
    private final List<Map<String, Object>> holdList = new ArrayList<>();

    // 생성자를 통해 초기 데이터 입력
    public HoldController() {
        Map<String, Object> hold1 = new HashMap<>();
        hold1.put("id", 1L);
        hold1.put("name", "저그 (Jug)");
        hold1.put("gripType", "jug");
        hold1.put("description", "손가락 전체로 깊게 잡을 수 있는 편한 홀드");

        Map<String, Object> hold2 = new HashMap<>();
        hold2.put("id", 2L);
        hold2.put("name", "슬로퍼 (Sloper)");
        hold2.put("gripType", "sloper");
        hold2.put("description", "손바닥 전체의 마찰력으로 잡는 둥근 홀드");

        Map<String, Object> hold3 = new HashMap<>();
        hold3.put("id", 3L);
        hold3.put("name", "크림프 (Crimp)");
        hold3.put("gripType", "crimp");
        hold3.put("description", "손가락 끝으로 얇게 걸쳐 잡는 홀드");

        holdList.add(hold1);
        holdList.add(hold2);
        holdList.add(hold3);
    }

    // 1. 전체 목록 및 Query Parameter 조건 검색 (필터링)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getHolds(@RequestParam(required = false) String grip) {
        // 쿼리 파라미터가 없으면 전체 목록 반환
        if (grip == null || grip.isEmpty()) {
            return ResponseEntity.ok(holdList);
        }

        // grip 조건이 있으면 해당 조건만 필터링해서 리스트 생성
        List<Map<String, Object>> filteredHolds = new ArrayList<>();
        for (Map<String, Object> hold : holdList) {
            if (grip.equalsIgnoreCase((String) hold.get("gripType"))) {
                filteredHolds.add(hold);
            }
        }

        return ResponseEntity.ok(filteredHolds);
    }

    // 2. Path Variable 단일 ID 검색 (자원 특정)
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getHoldById(@PathVariable Long id) {
        // ID가 일치하는 홀드 검색
        for (Map<String, Object> hold : holdList) {
            if (id.equals(hold.get("id"))) {
                return ResponseEntity.ok(hold); // 200 OK
            }
        }
        // 찾는 ID가 없을 경우 404 Not Found 반환
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 3. 새 홀드 등록 (POST) - 메모리 리스트에 진짜 추가
    @PostMapping
    public ResponseEntity<Map<String, Object>> createHold(@RequestBody Map<String, Object> newHold) {
        Long newId = (long) (holdList.size() + 1);
        newHold.put("id", newId);
        holdList.add(newHold);

        return ResponseEntity.status(HttpStatus.CREATED).body(newHold);
    }

    // 4. 홀드 삭제 (DELETE) - 메모리 리스트에서 진짜 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHold(@PathVariable Long id) {
        holdList.removeIf(hold -> id.equals(hold.get("id")));
        return ResponseEntity.noContent().build();
    }
}