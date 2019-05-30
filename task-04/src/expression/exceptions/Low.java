package expression.exceptions;

import expression.TripleExpression;

public class Low extends UnaryOperator {
    @Override
    protected int apply(int value) {
        return Integer.lowestOneBit(value);
    }

    public Low(TripleExpression expression) {
        super(expression);
    }
}
