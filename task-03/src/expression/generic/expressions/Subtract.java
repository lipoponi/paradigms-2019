package expression.generic.expressions;

import expression.generic.performers.Performer;

public class Subtract<T extends Number> extends BinaryOperator<T> {
    public Subtract(Performer<T> performer, TripleExpression<T> left, TripleExpression<T> right) {
        super(performer, left, right);
    }

    @Override
    protected T apply(T left, T right) {
        return performer.subtract(left, right);
    }
}
