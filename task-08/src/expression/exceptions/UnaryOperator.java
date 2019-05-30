package expression.exceptions;

import expression.TripleExpression;

public abstract class UnaryOperator implements TripleExpression {
    private final TripleExpression inner;

    protected abstract int apply(int value);

    public UnaryOperator(TripleExpression expression) {
        this.inner = expression;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int value = inner.evaluate(x, y, z);

        return apply(value);
    }
}