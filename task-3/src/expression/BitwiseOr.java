package expression;

public class BitwiseOr extends BinaryOperator {
    public BitwiseOr(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    protected int operationImpl(int left, int right) {
        return left | right;
    }

    protected double operationImpl(double left, double right) {
        return 0;
    }
}
