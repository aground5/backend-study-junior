package com.gdgku.attendance;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 10);
    private static final LocalTime ABSENT_CUTOFF = LocalTime.of(9, 30);

    private final List<Attendance> attendances = new ArrayList<>();
    private long nextId = 1L;

    /**
     * 출석 상태 판정 단일 진실 공급원(SSOT)
     * - 09:10 이하: ON_TIME
     * - 09:11 ~ 09:30: LATE
     * - 09:31 이후: ABSENT
     */
    public String determineStatus(LocalTime checkInTime) {
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
        Attendance attendance = new Attendance(
                nextId++,
                request.getStudentName(),
                request.getCheckInTime(),
                status
        );
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

    public Attendance updateCheckInTime(Long id, LocalTime newCheckInTime) {
        Attendance attendance = getAttendance(id);
        if (attendance == null) {
            return null;
        }

        attendance.setCheckInTime(newCheckInTime);
        attendance.setStatus(determineStatus(newCheckInTime));
        return attendance;
    }
}