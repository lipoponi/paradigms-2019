package expression.generic.expressions;

import expression.generic.performers.Performer;

public class Multiply<T extends Number> extends BinaryOperator<T> {
    public Multiply(Performer<T> performer, TripleExpression<T> left, TripleExpression<T> right) {
        super(performer, left, right);
    }

    @Override
    protected T apply(T left, T right) {
        return performer.multiply(left, right);
    }
}
