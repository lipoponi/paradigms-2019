package expression.exceptions.ownExceptions;

public class UnsupportedSymbolException extends ParseException {
    public UnsupportedSymbolException(char symbol, int position) {
        super(String.format("Unsupported symbol '%c' on position %d", symbol, position));
    }
}
