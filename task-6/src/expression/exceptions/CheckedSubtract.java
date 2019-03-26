package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.EvaluationException;
import expression.exceptions.ownExceptions.OverflowException;

public class CheckedSubtract extends CheckedBinaryOperator {
    @Override
    protected void check(int left, int right) throws EvaluationException {
        if (right < 0 && left - right < left || 0 < right && left < left - right) {
            throw new OverflowException("-", left, right);
        }
    }

    @Override
    protected int apply(int left, int right) {
        return left - right;
    }

    public CheckedSubtract(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
}
