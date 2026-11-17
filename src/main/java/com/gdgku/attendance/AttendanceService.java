package com.gdgku.attendance;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * [Service] 출석 관련 비즈니스 로직과 데이터 저장을 담당한다.
 */
@Service
public class AttendanceService {
    /**
     * 체크인 시각으로 출석 상태를 판정
     * 판정 규칙이 이 메서드 한 곳에만 있다
     */
    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 10);
    private static final LocalTime ABSENT_CUTOFF = LocalTime.of(9, 30);

    private final List<Attendance> attendances = new ArrayList<>();
    private long nextId = 1L;

    public static String determineStatus(LocalTime checkInTime) {
        if (!checkInTime.isAfter(LATE_CUTOFF)) { // 09:10은 포함
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

    public Attendance getAttendanceById(long id) {
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

    public Attendance updateCheckInTime(long id, Attendance request) {
        Attendance attendance = getAttendanceById(id);
        if (attendance == null) {
            return null;
        }

        attendance.setCheckInTime(request.getCheckInTime());

        String status = determineStatus(request.getCheckInTime());
        attendance.setStatus(status);

        return attendance;
    }
}
