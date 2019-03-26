package expression.exceptions.ownExceptions;

public class UnknownIdentifierException extends ParseException {
    public UnknownIdentifierException(String identifier) {
        super(String.format("Unknown identifier '%s'", identifier));
    }

    public UnknownIdentifierException(String identifier, int position) {
        super(String.format("Unknown identifier '%s' on position %d", identifier, position));
    }
}
