package expression.exceptions.ownExceptions;

public class OverflowException extends EvaluationException {
    public OverflowException() {
        super("Overflow");
    }

    public OverflowException(String operation, int operand) {
        super(String.format("Overflow for '%s' operation with operand '%d'", operation, operand));
    }

    public OverflowException(String operation, int operand1, int operand2) {
        super(String.format("Overflow for '%s', operation with operands '%d', '%d'", operation, operand1, operand2));
    }
}
