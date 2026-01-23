package com.spire.restbridge.util;

/**
 * Utility class for permission conversions.
 */
public final class PermissionUtils {

    private PermissionUtils() {
        // Utility class
    }

    /**
     * Converts a Documentum permission level to its label.
     *
     * @param permit the permission level (1-7)
     * @return the permission label
     */
    public static String permitToLabel(int permit) {
        return switch (permit) {
            case 1 -> "NONE";
            case 2 -> "BROWSE";
            case 3 -> "READ";
            case 4 -> "RELATE";
            case 5 -> "VERSION";
            case 6 -> "WRITE";
            case 7 -> "DELETE";
            default -> "UNKNOWN";
        };
    }

    /**
     * Converts a Documentum permission label to its numeric level.
     * The REST API returns permission as a string label.
     *
     * @param label the permission label (case-insensitive)
     * @return the permission level (1-7), or -1 if unknown
     */
    public static int labelToPermit(String label) {
        if (label == null || label.isEmpty()) {
            return -1;
        }
        return switch (label.toUpperCase()) {
            case "NONE" -> 1;
            case "BROWSE" -> 2;
            case "READ" -> 3;
            case "RELATE" -> 4;
            case "VERSION" -> 5;
            case "WRITE" -> 6;
            case "DELETE" -> 7;
            default -> -1;
        };
    }
}
