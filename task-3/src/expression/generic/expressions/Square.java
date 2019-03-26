package expression.generic.expressions;

import expression.generic.performers.Performer;

public class Square<T extends Number> extends UnaryOperator<T> {
    public Square(Performer<T> performer, TripleExpression<T> expression) {
        super(performer, expression);
    }

    @Override
    protected T apply(T value) {
        return performer.square(value);
    }
}
