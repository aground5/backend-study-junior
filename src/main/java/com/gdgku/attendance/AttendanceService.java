package com.gdgku.attendance;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service 
public class AttendanceService {

    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 10);
    private static final LocalTime ABSENT_CUTOFF = LocalTime.of(9, 30);

    private final List<Attendance> attendances = new ArrayList<>();
    private long nextId = 1L;

    public static String determineStatus(LocalTime checkInTime) {
        String status;
        if (!checkInTime.isAfter(LATE_CUTOFF)) {
            status = "ON_TIME";
        } else if (!checkInTime.isAfter(ABSENT_CUTOFF)) {
            status = "LATE";
        } else {
            status = "ABSENT";
        }

        return status;
    }
    


    public Attendance checkIn(@RequestBody Attendance request) {
        String status = determineStatus(request.getCheckInTime());

        Attendance attendance = new Attendance(nextId++, request.getStudentName(), request.getCheckInTime(), status);
        attendances.add(attendance);
        return attendance;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

  
    public Attendance getAttendance(@PathVariable Long id) {
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
   
    public Attendance updateCheckInTime(@PathVariable Long id, @RequestBody Attendance request) {
        Attendance attendance = getAttendance(id);
        if (attendance == null) {
            return null;
        }

        attendance.setCheckInTime(request.getCheckInTime());

        String status = determineStatus(attendance.getCheckInTime());
        attendance.setStatus(status);

        return attendance;
    }
}
