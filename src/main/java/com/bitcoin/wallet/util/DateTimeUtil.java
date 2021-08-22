package com.bitcoin.wallet.util;

import com.bitcoin.wallet.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** DateTimeUtil contains a bunch of utilities to work with datetime */
public class DateTimeUtil {
    public static final DateTimeFormatter formatterFromDB = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.0");
    public static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    public static final ZoneId zoneId = ZoneId.of("+00:00");

    public static ZonedDateTime toZonedDateTime(String dateTime) throws ValidationException {
        try {
            return ZonedDateTime.parse(dateTime, formatter).withZoneSameInstant(zoneId);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public static LocalDateTime toLocalDateTime(String dateTime) throws ValidationException {
        return toZonedDateTime(dateTime).toLocalDateTime();
    }

    public static LocalDateTime toLocalTimeFromDB(String dateTime) {
        return LocalDateTime.parse(dateTime, formatterFromDB);
    }

    public static String localTimeToString(LocalDateTime dateTime) {
        return  formatter.format(dateTime.atZone(zoneId));
    }
}
