package io.parsetra.size;

import io.parsetra.parser.NumericParser;
import io.parsetra.parser.Token;
import io.parsetra.parser.Tokenizer;
import io.parsetra.parser.UnitMatcher;
import io.parsetra.util.MathUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses human-readable data size strings into byte counts.
 * Decimal units (KB, MB, GB, TB) and binary (KiB, MiB, GiB, TiB). Thread-safe.
 */
public final class SizeParser {

    private static final long K = 1000L;
    private static final long KI = 1024L;

    private static final Map<String, Long> UNIT_TO_BYTES = new HashMap<>();
    static {
        UNIT_TO_BYTES.put("b", 1L);
        UNIT_TO_BYTES.put("byte", 1L);
        UNIT_TO_BYTES.put("bytes", 1L);
        UNIT_TO_BYTES.put("kb", K);
        UNIT_TO_BYTES.put("mb", K * K);
        UNIT_TO_BYTES.put("gb", K * K * K);
        UNIT_TO_BYTES.put("tb", K * K * K * K);
        UNIT_TO_BYTES.put("kib", KI);
        UNIT_TO_BYTES.put("mib", KI * KI);
        UNIT_TO_BYTES.put("gib", KI * KI * KI);
        UNIT_TO_BYTES.put("tib", KI * KI * KI * KI);
    }

    private static final String CONTEXT = "size";

    private SizeParser() {}

    /**
     * Parse with strict tokenization: single segment, no leading/trailing whitespace.
     */
    public static long parseStrict(String input) {
        List<Token> tokens = Tokenizer.tokenizeStrict(input, CONTEXT);
        return parseSingleToken(tokens, true);
    }

    /**
     * Parse with lenient tokenization: trim, optional spaces.
     */
    public static long parseLenient(String input) {
        List<Token> tokens = Tokenizer.tokenizeLenient(input, CONTEXT);
        return parseSingleToken(tokens, false);
    }

    /**
     * Default parse (lenient).
     */
    public static long parse(String input) {
        return parseLenient(input);
    }

    private static long parseSingleToken(List<Token> tokens, boolean strictNumber) {
        if (tokens.size() != 1) {
            throw new IllegalArgumentException("Size must be a single value with unit: expected 1 segment, got " + tokens.size());
        }
        Token t = tokens.get(0);
        double value = strictNumber
            ? NumericParser.parseDoubleStrict(t.getNumberPart(), CONTEXT)
            : NumericParser.parseDoubleLenient(t.getNumberPart(), CONTEXT);
        long bytesPerUnit = UnitMatcher.match(t.getUnitPart(), UNIT_TO_BYTES, CONTEXT);
        if (value < 0) {
            throw new IllegalArgumentException("Negative size not allowed: " + value);
        }
        double bytes = (double) bytesPerUnit * value;
        return MathUtils.roundToLong(bytes, CONTEXT);
    }
}
