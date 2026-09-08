package com.gdgku.attendance;

import java.time.LocalTime;

public class AttendanceService {

    private static final LocalTime LATE_CUTOFF = LocalTime.of(9, 10);
    private static final LocalTime ABSENT_CUTOFF = LocalTime.of(9, 30);

    public String determineStatus(LocalTime checkInTime) {
        if (!checkInTime.isAfter(LATE_CUTOFF)) {
            return "ON_TIME";
        } else if (!checkInTime.isAfter(ABSENT_CUTOFF)) {
            return "LATE";
        } else {
            return "ABSENT";
        }
    }
}