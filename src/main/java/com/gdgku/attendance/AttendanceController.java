package com.gdgku.attendance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 요청을 받아 AttendanceService에 위임하기만 하는 얇은 컨트롤러.
 * 데이터 저장과 지각 판정 규칙은 더 이상 여기 없다.
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public Attendance checkIn(@RequestBody Attendance request) {
        return attendanceService.checkIn(request.getStudentName(), request.getCheckInTime());
    }

    @GetMapping
    public List<Attendance> getAttendances() {
        return attendanceService.getAttendances();
    }

    @GetMapping("/{id}")
    public Attendance getAttendance(@PathVariable Long id) {
        return attendanceService.getAttendance(id);
    }

    @GetMapping("/late-count")
    public long countLate() {
        return attendanceService.countLate();
    }

    // 관리자가 잘못 입력된 출석 시각을 정정하는 API.
    @PutMapping("/{id}")
    public Attendance updateCheckInTime(@PathVariable Long id, @RequestBody Attendance request) {
        return attendanceService.updateCheckInTime(id, request.getCheckInTime());
    }
}
