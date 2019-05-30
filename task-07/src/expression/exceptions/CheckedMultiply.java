package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.EvaluationException;
import expression.exceptions.ownExceptions.OverflowException;

public class CheckedMultiply extends CheckedBinaryOperator {
    @Override
    protected void check(int left, int right) throws EvaluationException {
        int x = left * right;
        int min = (left < right) ? left : right;
        int max = (left < right) ? right : left;

        if (left != 0 && x / left != right || min == Integer.MIN_VALUE && max == -1) {
            throw new OverflowException("*", left, right);
        }
    }

    @Override
    protected int apply(int left, int right) {
        return left * right;
    }

    public CheckedMultiply(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
}
