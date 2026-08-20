package com.gdgku.study.backend.memo;

import com.gdgku.study.backend.memo.dto.MemoCreateRequest;
import com.gdgku.study.backend.memo.dto.MemoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
class MemoServiceRaceConditionTest {

    @Autowired
    private MemoService memoService;

    @Test
    @DisplayName("MemoService direct multi-thread concurrency test")
    void testRaceConditionDirectly(TestReporter reporter) throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<Long> returnedIds = Collections.synchronizedList(new ArrayList<>());
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        StringBuilder reportBuilder = new StringBuilder();
        appendLog(reportBuilder, "============================================================");
        appendLog(reportBuilder, "[+] Starting MemoService Direct Thread Concurrency Test");
        appendLog(reportBuilder, "[+] Concurrency Level (Threads): " + threadCount);
        appendLog(reportBuilder, "============================================================");

        long startTime = System.nanoTime();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    latch.await(); // Synchronize all threads to fire at the exact same moment
                    MemoResponse response = memoService.createMemo(new MemoCreateRequest("Title " + index, "Content " + index));
                    if (response != null && response.getId() != null) {
                        returnedIds.add(response.getId());
                    }
                } catch (Exception e) {
                    exceptions.add(e);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        latch.countDown(); // Trigger all threads simultaneously
        doneLatch.await();
        executorService.shutdown();

        long elapsedTimeMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

        int storedCount = memoService.getAllMemos().size();
        int successCalls = returnedIds.size();
        int failedCalls = exceptions.size();

        appendLog(reportBuilder, "[+] Concurrent execution completed in " + elapsedTimeMs + " ms.");
        appendLog(reportBuilder, "[+] Successful method calls: " + successCalls);
        appendLog(reportBuilder, "[+] Failed / Exception calls: " + failedCalls);
        appendLog(reportBuilder, "[+] Total Memos currently stored: " + storedCount);

        // Analyze Race Condition Results
        appendLog(reportBuilder, "\n============================================================");
        appendLog(reportBuilder, " RESULTS ANALYSIS ");
        appendLog(reportBuilder, "============================================================");

        Map<Long, Long> idCounts = returnedIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<Long, Long> duplicateIds = idCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        boolean raceDetected = false;

        if (!duplicateIds.isEmpty()) {
            raceDetected = true;
            appendLog(reportBuilder, " [!] RACE CONDITION DETECTED: Duplicate IDs assigned in responses!");
            duplicateIds.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> appendLog(reportBuilder, "    - ID " + entry.getKey() + ": assigned " + entry.getValue() + " times"));
        } else {
            appendLog(reportBuilder, " [✓] No duplicate IDs observed in return values.");
        }

        if (storedCount < threadCount) {
            raceDetected = true;
            appendLog(reportBuilder, " [!] RACE CONDITION DETECTED: Discrepancy between created count and stored count!");
            appendLog(reportBuilder, "    - Expected stored count: " + threadCount);
            appendLog(reportBuilder, "    - Actual stored count: " + storedCount);
            appendLog(reportBuilder, "    - Missing / overwritten entries: " + (threadCount - storedCount));
        } else {
            appendLog(reportBuilder, " [✓] Stored memo count matches expected thread count.");
        }

        if (!exceptions.isEmpty()) {
            raceDetected = true;
            appendLog(reportBuilder, " [!] RACE CONDITION DETECTED: Exceptions thrown during concurrent operations!");
            Map<String, Long> exceptionCounts = exceptions.stream()
                    .collect(Collectors.groupingBy(e -> e.getClass().getSimpleName(), Collectors.counting()));
            exceptionCounts.forEach((exName, count) -> appendLog(reportBuilder, "    - " + exName + ": " + count + " times"));
        }

        appendLog(reportBuilder, "============================================================");
        if (raceDetected) {
            appendLog(reportBuilder, " CONCLUSION: Race Condition reproduced!");
        } else {
            appendLog(reportBuilder, " CONCLUSION: No race condition detected (Thread-safe DB / JPA active).");
        }
        appendLog(reportBuilder, "============================================================");

        if (reporter != null) {
            reporter.publishEntry("Race Condition Report", reportBuilder.toString());
        }
        System.out.flush();
    }

    private void appendLog(StringBuilder builder, String msg) {
        builder.append(msg).append("\n");
        System.out.println(msg);
    }
}
