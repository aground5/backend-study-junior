package com.gdgku.attendance;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AttendanceService {
    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 10);
    private static final LocalTime ABSENT_CUTOFF = LocalTime.of(9, 30);

    private final List<Attendance> attendances = new ArrayList<>();
    private long nextId = 1L;

    public Attendance checkIn(Attendance request) {
        String status = determineStatus(request.getCheckInTime());

        Attendance attendance = new Attendance(nextId++, request.getStudentName(), request.getCheckInTime(), status);
        attendances.add(attendance);
        return attendance;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public Attendance getAttendance(Long id) {
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

    // 관리자가 잘못 입력된 출석 시각을 정정하는 API.
    // 지각 판정 로직을 checkIn()과 별개로 다시 구현하다가 경계값 조건(<= vs <)이 미묘하게 달라졌다.
    public Attendance updateCheckInTime(Long id, Attendance request) {
        Attendance attendance = getAttendance(id);
        if (attendance == null) {
            return null;
        }

        attendance.setCheckInTime(request.getCheckInTime());

        attendance.setStatus(determineStatus(request.getCheckInTime()));

        return attendance;
    }

    private String determineStatus(LocalTime checkInTime) {
        if (!checkInTime.isAfter(LATE_CUTOFF)) {
            return "ON_TIME";
        }
        if (!checkInTime.isAfter(ABSENT_CUTOFF)) {
            return "LATE";
        }
        return "ABSENT";
    }
}