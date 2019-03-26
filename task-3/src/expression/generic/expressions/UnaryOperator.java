package expression.generic.expressions;

import expression.generic.performers.Performer;

public abstract class UnaryOperator<T extends Number> implements TripleExpression<T> {
    private final TripleExpression<T> inner;
    protected final Performer<T> performer;

    protected abstract T apply(T value);

    public UnaryOperator(Performer<T> performer, TripleExpression<T> expression) {
        this.performer = performer;
        this.inner = expression;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return apply(inner.evaluate(x, y, z));
    }
}
