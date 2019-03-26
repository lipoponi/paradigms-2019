package expression.exceptions;

import expression.TripleExpression;

public class High extends UnaryOperator {
    @Override
    protected int apply(int value) {
        return Integer.highestOneBit(value);
    }

    public High(TripleExpression expression) {
        super(expression);
    }
}
