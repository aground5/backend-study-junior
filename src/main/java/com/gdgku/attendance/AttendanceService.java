package com.gdgku.attendance;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    public Attendance updateCheckInTime(Long id, Attendance request) {
        Attendance attendance = getAttendance(id);
        if (attendance == null) {
            return null;
        }

        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setStatus(determineStatus(request.getCheckInTime()));
        return attendance;
    }

    private static String determineStatus(LocalTime checkInTime) {
        if (!checkInTime.isAfter(LATE_CUTOFF)) {
            return "ON_TIME";
        } else if (!checkInTime.isAfter(ABSENT_CUTOFF)) {
            return "LATE";
        }
        return "ABSENT";
    }
}
