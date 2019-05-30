package expression.generic.expressions;

import expression.generic.performers.Performer;

public abstract class BinaryOperator<T extends Number> implements TripleExpression<T> {
    private final TripleExpression<T> left, right;
    protected final Performer<T> performer;

    protected abstract T apply(T left, T right);

    public BinaryOperator(Performer<T> performer, TripleExpression<T> left, TripleExpression<T> right) {
        this.performer = performer;
        this.left = left;
        this.right = right;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return apply(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}
