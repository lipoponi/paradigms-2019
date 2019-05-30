package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.DivisionByZeroException;
import expression.exceptions.ownExceptions.EvaluationException;
import expression.exceptions.ownExceptions.OverflowException;

public class CheckedDivide extends CheckedBinaryOperator {
    @Override
    protected void check(int left, int right) throws EvaluationException {
        if (right == 0) {
            throw new DivisionByZeroException();
        }

        if (right == -1 && left == Integer.MIN_VALUE) {
            throw new OverflowException("/", left, right);
        }
    }

    @Override
    protected int apply(int left, int right) {
        return left / right;
    }

    public CheckedDivide(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
}
