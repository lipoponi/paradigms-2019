package expression.generic.expressions;

import expression.generic.performers.Performer;

public class Mod<T extends Number> extends BinaryOperator<T> {
    public Mod(Performer<T> performer, TripleExpression<T> left, TripleExpression<T> right) {
        super(performer, left, right);
    }

    @Override
    protected T apply(T left, T right) {
        return performer.mod(left, right);
    }
}
