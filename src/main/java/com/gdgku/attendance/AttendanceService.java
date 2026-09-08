package com.gdgku.attendance;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 10);
    private static final LocalTime ABSENT_CUTOFF = LocalTime.of(9, 30);

    private final List<Attendance> attendances = new ArrayList<>();
    private long nextId = 1L;

    // 공통 로직으로 전환
    public static String determineStatus(LocalTime checkInTime) {
        if (!checkInTime.isAfter(LATE_CUTOFF)) {
            return "ON_TIME";
        } else if (!checkInTime.isAfter(ABSENT_CUTOFF)) {
            return "LATE";
        } else {
            return "ABSENT";
        }
    }

    public Attendance checkIn(Attendance request) {
        String status = determineStatus(request.getCheckInTime());

        Attendance attendance = new Attendance(nextId++, request.getStudentName(), request.getCheckInTime(), status);
        attendances.add(attendance);
        return attendance;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public Attendance getAttendanceById(Long id) {
        for (Attendance attendance : attendances) {
            if (attendance.getId().equals(id)) {
                return attendance;
            }
        }
        return null;
    }

    public long countLate() {
        long count = 0;
        for (Attendance attendance : attendances) {
            if ("LATE".equals(attendance.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public Attendance updateCheckInTime(Long id, Attendance request) {
        Attendance attendance = getAttendanceById(id);
        if (attendance == null) {
            return null;
        }

        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setStatus(determineStatus(request.getCheckInTime()));

        return attendance;
    }

    // 이 부근 뭔가뭔가 기입해줬어야 했는데...

    // 지각 판정 로직의 단일 진실 공급원(Single Source of Truth).
    // checkIn()과 updateCheckInTime() 모두 이 메서드만 사용한다.
    // private String determineStatus(LocalTime checkInTime) {
    //     if (!checkInTime.isAfter(LATE_CUTOFF)) {
    //         return "ON_TIME";
    //     } else if (!checkInTime.isAfter(ABSENT_CUTOFF)) {
    //         return "LATE";
    //     } else {
    //         return "ABSENT";
    //     }
    // }
}