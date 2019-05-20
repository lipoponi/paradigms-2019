package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.EvaluationException;
import expression.exceptions.ownExceptions.OverflowException;

public class CheckedAbs extends CheckedUnaryOperator {
    @Override
    protected void check(int value) throws EvaluationException {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("abs", value);
        }
    }

    @Override
    protected int apply(int value) {
        return 0 < value ? value : -value;
    }

    public CheckedAbs(TripleExpression expression) {
        super(expression);
    }
}
