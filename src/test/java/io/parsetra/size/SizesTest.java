package io.parsetra.size;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SizesTest {

    @Test
    void parseDecimal() {
        assertEquals(10_000_000L, Sizes.parse("10MB"));
        assertEquals(1_500_000_000L, Sizes.parse("1.5GB"));
        assertEquals(512_000L, Sizes.parse("512KB"));
        assertEquals(100L, Sizes.parse("100B"));
    }

    @Test
    void parseBinary() {
        assertEquals(2L * 1024 * 1024 * 1024, Sizes.parse("2GiB"));
        assertEquals(1536L, Sizes.parse("1.5KiB"));
    }

    @Test
    void parseLenient() {
        assertEquals(10_000_000L, Sizes.parseLenient(" 10 MB "));
        assertEquals(10_000_000L, Sizes.parseLenient("10mb"));
    }

    @Test
    void parseStrict() {
        assertEquals(10_000_000L, Sizes.parseStrict("10MB"));
        assertThrows(IllegalArgumentException.class, () -> Sizes.parseStrict(" 10MB "));
    }

    @Test
    void format() {
        assertEquals("1.5KB", Sizes.format(1536));
        assertEquals("10MB", Sizes.format(10_000_000));
        assertEquals("1.2GB", Sizes.format(1_200_000_000));
        assertEquals("0B", Sizes.format(0));
    }

    @Test
    void formatBinary() {
        assertEquals("1.5KiB", Sizes.formatBinary(1536));
        assertEquals("2GiB", Sizes.formatBinary(2L * 1024 * 1024 * 1024));
    }

    @Test
    void formatBytes() {
        assertEquals("1536B", Sizes.formatBytes(1536));
    }

    @Test
    void parseInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> Sizes.parse(""));
        assertThrows(IllegalArgumentException.class, () -> Sizes.parse("10"));
        assertThrows(IllegalArgumentException.class, () -> Sizes.parse("10MX"));
    }
}
