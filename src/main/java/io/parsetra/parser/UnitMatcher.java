package io.parsetra.parser;

import java.util.Locale;
import java.util.Map;

/**
 * Matches unit strings against a registry and returns the associated value.
 * Case-insensitive. Thread-safe.
 */
public final class UnitMatcher {

    private UnitMatcher() {}

    /**
     * Returns the value for the given unit from the map, or throws if unknown.
     * Unit is trimmed and lowercased for lookup.
     */
    public static <T> T match(String unit, Map<String, T> unitMap, String context) {
        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit must not be empty in " + context);
        }
        String normalized = unit.trim().toLowerCase(Locale.ROOT);
        if (!unitMap.containsKey(normalized)) {
            throw new IllegalArgumentException("Unknown unit in " + context + ": '" + unit + "'");
        }
        return unitMap.get(normalized);
    }
}
