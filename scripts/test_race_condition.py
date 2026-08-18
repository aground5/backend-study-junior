#!/usr/bin/env python3
"""
Spring Boot Memo App Race Condition Test Script
Tests for race conditions in concurrent ID generation and list storage.
"""

import json
import time
import urllib.request
import urllib.error
import concurrent.futures
from collections import Counter

import sys

BASE_URL = "http://localhost:8080/memos"
DEFAULT_TOTAL_REQUESTS = 2000
DEFAULT_CONCURRENCY = 200

def create_memo(index: int) -> dict:
    url = BASE_URL
    payload = json.dumps({"title": f"Race Test {index}", "content": "Testing race condition"}).encode("utf-8")
    req = urllib.request.Request(
        url,
        data=payload,
        headers={"Content-Type": "application/json"},
        method="POST"
    )
    try:
        with urllib.request.urlopen(req, timeout=5) as resp:
            data = json.loads(resp.read().decode("utf-8"))
            return {"status": resp.status, "data": data, "error": None}
    except Exception as e:
        return {"status": getattr(e, "code", 500), "data": None, "error": str(e)}

def get_all_memos() -> list:
    req = urllib.request.Request(BASE_URL, method="GET")
    try:
        with urllib.request.urlopen(req, timeout=5) as resp:
            return json.loads(resp.read().decode("utf-8"))
    except Exception as e:
        print(f"[-] Failed to fetch all memos: {e}")
        return []

def main():
    total_requests = int(sys.argv[1]) if len(sys.argv) > 1 else DEFAULT_TOTAL_REQUESTS
    concurrency = int(sys.argv[2]) if len(sys.argv) > 2 else DEFAULT_CONCURRENCY

    print("=" * 60)
    print(f"[+] Starting Race Condition Test")
    print(f"[+] Target URL: {BASE_URL}")
    print(f"[+] Total Requests: {total_requests}, Concurrency Level: {concurrency}")
    print("=" * 60)

    start_time = time.time()
    created_ids = []
    failed_count = 0

    with concurrent.futures.ThreadPoolExecutor(max_workers=concurrency) as executor:
        futures = [executor.submit(create_memo, i) for i in range(total_requests)]
        for future in concurrent.futures.as_completed(futures):
            res = future.result()
            if res["status"] == 200 and res["data"] and "id" in res["data"]:
                created_ids.append(res["data"]["id"])
            else:
                failed_count += 1

    elapsed = time.time() - start_time
    print(f"[+] Concurrent requests completed in {elapsed:.2f} seconds.")
    print(f"[+] Successful POST responses: {len(created_ids)}")
    print(f"[+] Failed POST responses: {failed_count}")

    # Fetch stored memos from server
    all_memos = get_all_memos()
    print(f"[+] Total Memos currently stored in server: {len(all_memos)}")

    # Analyze Race Condition Results
    print("\n" + "=" * 60)
    print(" RESULTS ANALYSIS ")
    print("=" * 60)

    id_counts = Counter(created_ids)
    duplicate_ids = {memo_id: count for memo_id, count in id_counts.items() if count > 1}

    race_detected = False

    if duplicate_ids:
        race_detected = True
        print(f" [!] RACE CONDITION DETECTED: Duplicate IDs assigned in responses!")
        for memo_id, count in sorted(duplicate_ids.items()):
            print(f"    - ID {memo_id}: assigned {count} times")
    else:
        print(" [✓] No duplicate IDs observed in POST responses.")

    if len(all_memos) != len(created_ids):
        race_detected = True
        print(f" [!] RACE CONDITION DETECTED: Discrepancy between created count and stored count!")
        print(f"    - Expected stored count: {len(created_ids)}")
        print(f"    - Actual stored count: {len(all_memos)}")
        print(f"    - Missing / overwritten entries: {abs(len(created_ids) - len(all_memos))}")
    else:
        print(" [✓] Stored memo count matches POST response count.")

    print("=" * 60)
    if race_detected:
        print(" CONCLUSION: Race Condition successfully reproduced!")
    else:
        print(" CONCLUSION: No race condition detected. (Server might be single-threaded or DB transaction applied).")
    print("=" * 60)

if __name__ == "__main__":
    main()
