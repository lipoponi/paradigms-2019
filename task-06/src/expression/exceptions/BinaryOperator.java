package expression.exceptions;

import expression.TripleExpression;

public abstract class BinaryOperator implements TripleExpression {
    private final TripleExpression left, right;

    protected abstract int apply(int left, int right);

    public BinaryOperator(TripleExpression left, TripleExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int leftValue = left.evaluate(x, y, z);
        int rightValue = right.evaluate(x, y, z);

        return apply(leftValue, rightValue);
    }
}
