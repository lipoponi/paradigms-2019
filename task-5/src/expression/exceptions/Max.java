package expression.exceptions;

import expression.TripleExpression;

public class Max extends BinaryOperator {
    @Override
    protected int apply(int left, int right) {
        return left < right ? right : left;
    }

    public Max(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
}
