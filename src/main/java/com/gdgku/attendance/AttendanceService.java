package com.gdgku.attendance;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AttendanceService {
    
    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 10);
    private static final LocalTime ABSENT_CUTOFF = LocalTime.of(9, 30);

    private final List<Attendance> attendances = new ArrayList<>();
    private long nextId = 1L;

    public static String determineStatus(String checkInTime) {
        LocalTime time = LocalTime.parse(checkInTime);
        if (!time.isAfter(LATE_CUTOFF)) {
            return "ON_TIME";
        }
        if (!time.isAfter(ABSENT_CUTOFF)) {
            return "LATE";
        }
        return "ABSENT";
    }


    public Attendance checkIn(@RequestBody Attendance request) {
        Attendance attendance = new Attendance(nextId++, request.getStudentName(), request.getCheckInTime(),
                determineStatus(request.getCheckInTime()));
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

    private String calculateStatus(String checkInTime) {
        LocalTime time = LocalTime.parse(checkInTime);
        if (!time.isAfter(LATE_CUTOFF)) {
            return "ON_TIME";
        }
        if (!time.isAfter(ABSENT_CUTOFF)) {
            return "LATE";
        }
        return "ABSENT";
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
    
    public Attendance updateCheckInTime(@PathVariable Long id, @RequestBody Attendance request) {
        Attendance attendance = getAttendance(id);
        if (attendance == null) {
            return null;
        }

        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setStatus(calculateStatus(request.getCheckInTime()));

        return attendance;
    }


}
