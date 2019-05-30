package expression.exceptions;

import expression.TripleExpression;

public class Const implements TripleExpression {
    private final int value;

    public Const(int value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }
}
