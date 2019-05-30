package expression.generic.expressions;

import expression.generic.performers.Performer;

public class Abs<T extends Number> extends UnaryOperator<T> {
    public Abs(Performer<T> performer, TripleExpression<T> expression) {
        super(performer, expression);
    }

    @Override
    protected T apply(T value) {
        return performer.abs(value);
    }
}
