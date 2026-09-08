package com.gdgku.attendance;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 출석 도메인의 비즈니스 로직을 담당하는 계층.
 *
 * "지각 판정"은 오직 judgeStatus() 하나에만 존재한다(단일 진실 공급원).
 * 체크인이든 관리자 정정이든 같은 시각이면 반드시 같은 상태가 나온다.
 */
@Service
public class AttendanceService {

    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 10);
    private static final LocalTime ABSENT_CUTOFF = LocalTime.of(9, 30);

    private final List<Attendance> attendances = new ArrayList<>();
    private long nextId = 1L;

    /**
     * 지각 판정 규칙.
     * 09:10까지는 정시, 09:30까지는 지각, 그 이후는 결석.
     * (경계값 09:10 = ON_TIME, 09:30 = LATE)
     */
    public String judgeStatus(LocalTime checkInTime) {
        if (!checkInTime.isAfter(LATE_CUTOFF)) {
            return "ON_TIME";
        }
        if (!checkInTime.isAfter(ABSENT_CUTOFF)) {
            return "LATE";
        }
        return "ABSENT";
    }

    public Attendance checkIn(String studentName, LocalTime checkInTime) {
        Attendance attendance = new Attendance(nextId++, studentName, checkInTime, judgeStatus(checkInTime));
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

    /**
     * 관리자가 잘못 입력된 출석 시각을 정정한다.
     * 판정은 checkIn()과 똑같이 judgeStatus()에 위임하므로 두 API의 결과가 어긋날 수 없다.
     */
    public Attendance updateCheckInTime(Long id, LocalTime checkInTime) {
        Attendance attendance = getAttendance(id);
        if (attendance == null) {
            return null;
        }

        attendance.setCheckInTime(checkInTime);
        attendance.setStatus(judgeStatus(checkInTime));
        return attendance;
    }
}
