package com.FlightSearch.breakabletoy2.util;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class FlightFilterUtils {

    private static final List<String> VALID_TRAVEL_CLASSES = Arrays.asList(
            "ECONOMY", "PREMIUM_ECONOMY", "BUSINESS", "FIRST"
    );

    private static final List<String> VALID_SORT_FIELDS = Arrays.asList(
            "price", "duration", "departure", "arrival", "stops", "airline"
    );

    private static final List<String> VALID_SORT_ORDERS = Arrays.asList(
            "asc", "desc"
    );

    private static final List<String> VALID_CURRENCIES = Arrays.asList(
            "USD", "EUR", "MXN"
    );

    public static boolean isValidTravelClass(String travelClass) {
        return travelClass == null || VALID_TRAVEL_CLASSES.contains(travelClass.toUpperCase());
    }

    public static boolean isValidSortField(String sortField) {
        return sortField == null || VALID_SORT_FIELDS.contains(sortField.toLowerCase());
    }

    public static boolean isValidSortOrder(String sortOrder) {
        return sortOrder == null || VALID_SORT_ORDERS.contains(sortOrder.toLowerCase());
    }

    public static boolean isValidCurrency(String currency) {
        return currency == null || VALID_CURRENCIES.contains(currency.toUpperCase());
    }

    public static boolean isValidAirlineCode(String airlineCode) {
        return airlineCode != null &&
                airlineCode.length() == 2 &&
                airlineCode.matches("[A-Z0-9]{2}");
    }

    public static boolean isValidTimeFormat(String timeStr) {
        if (timeStr == null) return true;

        try {
            LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static Duration parseDurationSafely(String durationStr) {
        if (durationStr == null || !durationStr.startsWith("PT")) {
            return null;
        }

        try {
            return Duration.parse(durationStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalTime parseTimeSafely(String timeStr) {
        if (timeStr == null) return null;

        try {
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            try {
                return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

    public static String normalizeTravelClass(String travelClass) {
        if (travelClass == null) return null;

        String normalized = travelClass.toUpperCase().trim();

        switch (normalized) {
            case "ECONOMY":
            case "ECO":
            case "Y":
                return "ECONOMY";
            case "PREMIUM_ECONOMY":
            case "PREMIUM":
            case "PREM":
            case "W":
                return "PREMIUM_ECONOMY";
            case "BUSINESS":
            case "BUS":
            case "C":
            case "J":
                return "BUSINESS";
            case "FIRST":
            case "F":
                return "FIRST";
            default:
                return normalized;
        }
    }

    public static String formatDuration(Duration duration) {
        if (duration == null) return "N/A";

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        if (hours > 0) {
            return String.format("%dh %02dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }

    public static String formatTime(LocalTime time) {
        if (time == null) return "N/A";
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static boolean isValidStopsRange(Integer maxStops) {
        return maxStops == null || (maxStops >= 0 && maxStops <= 5);
    }

    public static boolean isValidDurationRange(Integer maxDurationHours) {
        return maxDurationHours == null || (maxDurationHours >= 1 && maxDurationHours <= 48);
    }

    public static boolean isTimeInRange(LocalTime time, LocalTime earliest, LocalTime latest) {
        if (time == null) return true;
        if (earliest == null && latest == null) return true;

        if (earliest != null && time.isBefore(earliest)) {
            return false;
        }

        if (latest != null && time.isAfter(latest)) {
            return false;
        }

        return true;
    }

    public static String extractTimeFromDateTime(String dateTimeStr) {
        if (dateTimeStr == null || !dateTimeStr.contains("T")) {
            return null;
        }

        try {
            String timeStr = dateTimeStr.split("T")[1];
            if (timeStr.contains("+") || timeStr.contains("-")) {
                timeStr = timeStr.split("[+-]")[0];
            }

            LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
            return time.format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> getValidTravelClasses() {
        return VALID_TRAVEL_CLASSES;
    }

    public static List<String> getValidSortFields() {
        return VALID_SORT_FIELDS;
    }

    public static List<String> getValidSortOrders() {
        return VALID_SORT_ORDERS;
    }

    public static List<String> getValidCurrencies() {
        return VALID_CURRENCIES;
    }
}
