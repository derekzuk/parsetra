package io.parsetra.parser;

/**
 * A single number+unit pair produced by the tokenizer.
 */
public final class Token {
    private final String numberPart;
    private final String unitPart;

    public Token(String numberPart, String unitPart) {
        this.numberPart = numberPart;
        this.unitPart = unitPart;
    }

    public String getNumberPart() {
        return numberPart;
    }

    public String getUnitPart() {
        return unitPart;
    }
}
