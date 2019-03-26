package expression.exceptions.ownExceptions;

public class ConstOverflowException extends ParseException {
    public ConstOverflowException(String number, int position) {
        super(String.format("Constant overflow for '%s' on position %d", number, position));
    }
}
