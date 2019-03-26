package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.EvaluationException;
import expression.exceptions.ownExceptions.OverflowException;

public class CheckedAdd extends CheckedBinaryOperator {
    @Override
    protected void check(int left, int right) throws EvaluationException {
        if (right < 0 && left < left + right || 0 < right && left + right < left) {
            throw new OverflowException("+", left, right);
        }
    }

    @Override
    protected int apply(int left, int right) {
        return left + right;
    }

    public CheckedAdd(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
}
