package io.parsetra.duration;

import io.parsetra.parser.NumericParser;
import io.parsetra.parser.Token;
import io.parsetra.parser.Tokenizer;
import io.parsetra.parser.UnitMatcher;
import io.parsetra.util.MathUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses human-readable duration strings into {@link java.time.Duration}.
 * Units: ms, s, m, h, d. Thread-safe.
 */
public final class DurationParser {

    private static final long NANOS_PER_MS = 1_000_000L;
    private static final long NANOS_PER_S = 1_000_000_000L;
    private static final long NANOS_PER_M = 60 * NANOS_PER_S;
    private static final long NANOS_PER_H = 60 * NANOS_PER_M;
    private static final long NANOS_PER_D = 24 * NANOS_PER_H;

    private static final Map<String, Long> UNIT_TO_NANOS = new HashMap<>();
    static {
        UNIT_TO_NANOS.put("ms", NANOS_PER_MS);
        UNIT_TO_NANOS.put("s", NANOS_PER_S);
        UNIT_TO_NANOS.put("m", NANOS_PER_M);
        UNIT_TO_NANOS.put("min", NANOS_PER_M);
        UNIT_TO_NANOS.put("mins", NANOS_PER_M);
        UNIT_TO_NANOS.put("minute", NANOS_PER_M);
        UNIT_TO_NANOS.put("minutes", NANOS_PER_M);
        UNIT_TO_NANOS.put("h", NANOS_PER_H);
        UNIT_TO_NANOS.put("hr", NANOS_PER_H);
        UNIT_TO_NANOS.put("hrs", NANOS_PER_H);
        UNIT_TO_NANOS.put("hour", NANOS_PER_H);
        UNIT_TO_NANOS.put("hours", NANOS_PER_H);
        UNIT_TO_NANOS.put("d", NANOS_PER_D);
        UNIT_TO_NANOS.put("day", NANOS_PER_D);
        UNIT_TO_NANOS.put("days", NANOS_PER_D);
    }

    private static final String CONTEXT = "duration";

    private DurationParser() {}

    /**
     * Parse with strict tokenization (no leading/trailing whitespace, single space between segments).
     */
    public static Duration parseStrict(String input) {
        List<Token> tokens = Tokenizer.tokenizeStrict(input, CONTEXT);
        return parseTokens(tokens, true);
    }

    /**
     * Parse with lenient tokenization (trim, multiple spaces allowed).
     */
    public static Duration parseLenient(String input) {
        List<Token> tokens = Tokenizer.tokenizeLenient(input, CONTEXT);
        return parseTokens(tokens, false);
    }

    /**
     * Default parse: same as lenient for human-friendly input.
     */
    public static Duration parse(String input) {
        return parseLenient(input);
    }

    private static Duration parseTokens(List<Token> tokens, boolean strictNumber) {
        long totalNanos = 0;
        for (Token t : tokens) {
            double value = strictNumber
                ? NumericParser.parseDoubleStrict(t.getNumberPart(), CONTEXT)
                : NumericParser.parseDoubleLenient(t.getNumberPart(), CONTEXT);
            long nanosPerUnit = UnitMatcher.match(t.getUnitPart(), UNIT_TO_NANOS, CONTEXT);
            if (value < 0) {
                throw new IllegalArgumentException("Negative duration value not allowed: " + value);
            }
            double nanos = (double) nanosPerUnit * value;
            long add = MathUtils.roundToLong(nanos, CONTEXT);
            totalNanos = MathUtils.addExactOrThrow(totalNanos, add, CONTEXT);
        }
        return Duration.ofNanos(totalNanos);
    }
}
