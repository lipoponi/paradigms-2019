package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.EvaluationException;

public abstract class CheckedBinaryOperator implements TripleExpression {
    private final TripleExpression left, right;

    protected abstract void check(int left, int right) throws EvaluationException;

    protected abstract int apply(int left, int right);

    public CheckedBinaryOperator(TripleExpression left, TripleExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int leftValue = left.evaluate(x, y, z);
        int rightValue = right.evaluate(x, y, z);

        check(leftValue, rightValue);
        return apply(leftValue, rightValue);
    }
}
