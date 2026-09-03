package com.gdgku.study.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/my-stock-storages")
public class MyStockStorage {

    private final List<StockItem> stockList = new ArrayList<>();
    private long nextId = 1L;

    public static class StockItem {
        private Long id;
        private String name;
        private Long price;
        private Integer quantity;

        public StockItem() {}

        public StockItem(Long id, String name, Long price, Integer quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getPrice() { return price; }
        public void setPrice(Long price) { this.price = price; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    // 매수 기록 등록
    @PostMapping
    public ResponseEntity<StockItem> addStock(@RequestBody StockItem stockItem) {
        stockItem.setId(nextId++);
        stockList.add(stockItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(stockItem);
    }

    // 전체 매수 기록 조회
    @GetMapping
    public ResponseEntity<List<StockItem>> getAllStocks(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.ok(stockList);
        }

        List<StockItem> filteredList = new ArrayList<>();
        for (StockItem item : stockList) {
            if (item.getName() != null && item.getName().equalsIgnoreCase(name)) {
                filteredList.add(item);
            }
        }
        return ResponseEntity.ok(filteredList);
    }

    // 개별 종목 조회
    @GetMapping("/{id}")
    public ResponseEntity<StockItem> getStockById(@PathVariable Long id) {
        for (StockItem item : stockList) {
            if (item.getId().equals(id)) {
                return ResponseEntity.ok(item);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 매수 기록 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        boolean removed = stockList.removeIf(item -> item.getId().equals(id));
        if (!removed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
}