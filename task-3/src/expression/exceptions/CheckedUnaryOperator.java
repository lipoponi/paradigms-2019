package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.EvaluationException;

public abstract class CheckedUnaryOperator implements TripleExpression {
    private final TripleExpression inner;

    protected abstract void check(int value) throws EvaluationException;

    protected abstract int apply(int value);

    public CheckedUnaryOperator(TripleExpression expression) {
        this.inner = expression;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int value = inner.evaluate(x, y, z);

        check(value);
        return apply(value);
    }
}
