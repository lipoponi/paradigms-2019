package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.EvaluationException;
import expression.exceptions.ownExceptions.IllegalArgumentException;

public class CheckedSqrt extends CheckedUnaryOperator {
    @Override
    protected void check(int value) throws EvaluationException {
        if (value < 0) {
            throw new IllegalArgumentException(value, "sqrt");
        }
    }

    @Override
    protected int apply(int value) {
        if (value < 2) {
            return value;
        }

        int left = 0, right = value;

        while (left < right - 1) {
            int mid = left + (right - left) / 2;

            if (mid == 0 || mid != 0 && mid <= value / mid) {
                left = mid;
            } else {
                right = mid;
            }
        }

        return left;
    }

    public CheckedSqrt(TripleExpression expression) {
        super(expression);
    }
}
