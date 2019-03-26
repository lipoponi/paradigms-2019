package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.EvaluationException;
import expression.exceptions.ownExceptions.OverflowException;

public class CheckedNegate extends CheckedUnaryOperator {
    @Override
    protected void check(int value) throws EvaluationException {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("-", value);
        }
    }

    @Override
    protected int apply(int value) {
        return -value;
    }

    public CheckedNegate(TripleExpression expression) {
        super(expression);
    }
}
