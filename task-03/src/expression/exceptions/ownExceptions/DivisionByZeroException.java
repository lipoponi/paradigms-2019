package expression.exceptions.ownExceptions;

public class DivisionByZeroException extends EvaluationException {
    public DivisionByZeroException() {
        super("Division by zero in expression");
    }
}
