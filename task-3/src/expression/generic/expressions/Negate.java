package expression.generic.expressions;

import expression.generic.performers.Performer;

public class Negate<T extends Number> extends UnaryOperator<T> {
    public Negate(Performer<T> performer, TripleExpression<T> expression) {
        super(performer, expression);
    }

    @Override
    protected T apply(T value) {
        return performer.negate(value);
    }
}
