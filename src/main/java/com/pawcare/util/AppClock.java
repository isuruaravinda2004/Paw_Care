package com.pawcare.util;
import java.time.*;
public final class AppClock {
    private AppClock() { }
    public static final ZoneId ZONE = ZoneId.of("Asia/Colombo");
    public static LocalDate today() { return LocalDate.now(ZONE); }
    public static LocalDateTime now() { return LocalDateTime.now(ZONE); }
}
