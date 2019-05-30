package expression.exceptions;

import expression.TripleExpression;

public class Min extends BinaryOperator {

    @Override
    protected int apply(int left, int right) {
        return left < right ? left : right;
    }

    public Min(TripleExpression left, TripleExpression right) {
        super(left, right);
    }
}
