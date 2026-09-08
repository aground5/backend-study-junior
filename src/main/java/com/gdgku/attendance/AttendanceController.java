package com.gdgku.attendance;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    private final List<Attendance> attendances = new ArrayList<>();
    private long nextId = 1L;

    @PostMapping("/check-in")
    public Attendance checkIn(@RequestBody Attendance request) {
        String status = attendanceService.determineStatus(request.getCheckInTime());
        Attendance attendance = new Attendance(nextId++, request.getStudentName(), request.getCheckInTime(), status);
        attendances.add(attendance);
        return attendance;
    }

    @GetMapping
    public List<Attendance> getAttendances() {
        return attendances;
    }

    @GetMapping("/{id}")
    public Attendance getAttendance(@PathVariable Long id) {
        for (Attendance attendance : attendances) {
            if (attendance.getId().equals(id)) {
                return attendance;
            }
        }
        return null;
    }

    @GetMapping("/late-count")
    public long countLate() {
        long count = 0;
        for (Attendance attendance : attendances) {
            if ("LATE".equals(attendance.getStatus())) {
                count++;
            }
        }
        return count;
    }

    @PutMapping("/{id}")
    public Attendance updateCheckInTime(@PathVariable Long id, @RequestBody Attendance request) {
        Attendance attendance = getAttendance(id);
        if (attendance == null) {
            return null;
        }
        attendance.setCheckInTime(request.getCheckInTime());
        String status = attendanceService.determineStatus(request.getCheckInTime());
        attendance.setStatus(status);
        return attendance;
    }

    // ↓↓↓ 새로 추가: 최상위 Attendance를 상속하는 nested 클래스
    public static class Attendance extends com.gdgku.attendance.Attendance {
        public Attendance() {
            super();
        }

        public Attendance(Long id, String studentName, java.time.LocalTime checkInTime, String status) {
            super(id, studentName, checkInTime, status);
        }
    }
}