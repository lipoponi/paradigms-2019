package expression.exceptions.ownExceptions;

public class IllegalArgumentException extends EvaluationException {
    public IllegalArgumentException(int value, String operation) {
        super(String.format("Illegal argument '%d' for '%s' operation", value, operation));
    }
}
