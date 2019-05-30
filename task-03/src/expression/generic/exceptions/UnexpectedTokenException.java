package expression.generic.exceptions;

public class UnexpectedTokenException extends ParseException {
    public UnexpectedTokenException(String token) {
        super(String.format("Unexpected token '%s'", token));
    }

    public UnexpectedTokenException(String token, int position) {
        super(String.format("Unexpected token '%s' on position %d", token, position));
    }
}
