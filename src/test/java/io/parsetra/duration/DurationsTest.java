package io.parsetra.duration;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DurationsTest {

    @Test
    void parseSingleUnit() {
        assertEquals(Duration.ofHours(2), Durations.parse("2h"));
        assertEquals(Duration.ofMinutes(30), Durations.parse("30m"));
        assertEquals(Duration.ofMinutes(90), Durations.parse("90m"));
        assertEquals(Duration.ofSeconds(45), Durations.parse("45s"));
        assertEquals(Duration.ofMillis(1500), Durations.parse("1500ms"));
        assertEquals(Duration.ofDays(1), Durations.parse("1d"));
    }

    @Test
    void parseMultiSegment() {
        assertEquals(Duration.ofHours(2).plusMinutes(30), Durations.parse("2h 30m"));
        assertEquals(Duration.ofHours(2).plusMinutes(30), Durations.parse("2h30m"));
        assertEquals(Duration.ofDays(1).plusHours(4).plusMinutes(10), Durations.parse("1d 4h 10m"));
    }

    @Test
    void parseFractional() {
        assertEquals(Duration.ofHours(1).plusMinutes(30), Durations.parse("1.5h"));
        assertEquals(Duration.ofDays(1).plusHours(12), Durations.parse("1.5d"));
    }

    @Test
    void parseLenient() {
        assertEquals(Duration.ofMinutes(90), Durations.parseLenient("  90m  "));
        assertEquals(Duration.ofHours(2).plusMinutes(30), Durations.parseLenient("2h   30m"));
    }

    @Test
    void parseStrict() {
        assertEquals(Duration.ofHours(2).plusMinutes(30), Durations.parseStrict("2h30m"));
        assertEquals(Duration.ofHours(2).plusMinutes(30), Durations.parseStrict("2h 30m"));
        assertThrows(IllegalArgumentException.class, () -> Durations.parseStrict("  2h  "));
        assertThrows(IllegalArgumentException.class, () -> Durations.parseStrict("2h  30m"));
    }

    @Test
    void format() {
        assertEquals("2h30m", Durations.format(Duration.ofMinutes(150)));
        assertEquals("1h30m", Durations.format(Duration.ofMinutes(90)));
        assertEquals("1d", Durations.format(Duration.ofDays(1)));
        assertEquals("0s", Durations.format(Duration.ZERO));
    }

    @Test
    void formatCompact() {
        assertEquals("150m", Durations.formatCompact(Duration.ofMinutes(150)));
        assertEquals("2h", Durations.formatCompact(Duration.ofHours(2)));
    }

    @Test
    void formatHuman() {
        assertEquals("2h 30m", Durations.formatHuman(Duration.ofMinutes(150)));
        assertEquals("1d 4h 10m", Durations.formatHuman(Duration.ofDays(1).plusHours(4).plusMinutes(10)));
    }

    @Test
    void parseInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> Durations.parse(""));
        assertThrows(IllegalArgumentException.class, () -> Durations.parse("invalid"));
        assertThrows(IllegalArgumentException.class, () -> Durations.parse("2x"));
    }
}
