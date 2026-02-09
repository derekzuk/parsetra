package io.parsetra;

import io.parsetra.duration.Durations;
import io.parsetra.size.Sizes;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class EdgeCaseTest {

    // --- Duration edge cases ---

    @Test
    void durationWhitespace() {
        assertEquals(Duration.ofMinutes(90), Durations.parse("  90m  "));
        assertEquals(Duration.ofHours(2).plusMinutes(30), Durations.parse("2h   30m"));
        assertEquals(Duration.ofSeconds(1), Durations.parse("  1s  "));
    }

    @Test
    void durationCaseInsensitive() {
        assertEquals(Duration.ofHours(2), Durations.parse("2H"));
        assertEquals(Duration.ofMinutes(30), Durations.parse("30M"));
        assertEquals(Duration.ofSeconds(45), Durations.parse("45S"));
        assertEquals(Duration.ofDays(1), Durations.parse("1D"));
        assertEquals(Duration.ofMillis(500), Durations.parse("500MS"));
    }

    @Test
    void durationFractional() {
        assertEquals(Duration.ofMinutes(30), Durations.parse("0.5h"));
        assertEquals(Duration.ofHours(1).plusMinutes(6), Durations.parse("1.1h"));
    }

    @Test
    void durationInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> Durations.parse(""));
        assertThrows(IllegalArgumentException.class, () -> Durations.parse("   "));
        assertThrows(IllegalArgumentException.class, () -> Durations.parse("xyz"));
        assertThrows(IllegalArgumentException.class, () -> Durations.parse("2x"));
        assertThrows(IllegalArgumentException.class, () -> Durations.parse("2h 30"));
    }

    @Test
    void durationOverflow() {
        assertThrows(IllegalArgumentException.class, () ->
            Durations.parse("999999999999999d"));
    }

    // --- Size edge cases ---

    @Test
    void sizeWhitespace() {
        assertEquals(10_000_000L, Sizes.parseLenient(" 10 MB "));
        assertEquals(1024L, Sizes.parseLenient(" 1 KiB "));
    }

    @Test
    void sizeCaseInsensitive() {
        assertEquals(10_000_000L, Sizes.parse("10mb"));
        assertEquals(10_000_000L, Sizes.parse("10MB"));
        assertEquals(10_000_000L, Sizes.parse("10Mb"));
        assertEquals(2L * 1024 * 1024 * 1024, Sizes.parse("2gib"));
        assertEquals(2L * 1024 * 1024 * 1024, Sizes.parse("2GiB"));
    }

    @Test
    void sizeFractional() {
        assertEquals(1536L, Sizes.parse("1.5KiB"));
        assertEquals(1_500_000_000L, Sizes.parse("1.5GB"));
        assertEquals(512L, Sizes.parse("0.5KiB"));
    }

    @Test
    void sizeInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> Sizes.parse(""));
        assertThrows(IllegalArgumentException.class, () -> Sizes.parse("10"));
        assertThrows(IllegalArgumentException.class, () -> Sizes.parse("10MX"));
        assertThrows(IllegalArgumentException.class, () -> Sizes.parse("xyz"));
    }

    @Test
    void sizeOverflow() {
        assertThrows(IllegalArgumentException.class, () ->
            Sizes.parse("999999999999999TB"));
    }
}
