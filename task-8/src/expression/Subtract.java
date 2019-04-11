package expression;

public class Subtract extends BinaryOperator {
    public Subtract(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    protected int operationImpl(int left, int right) {
        return left - right;
    }

    protected double operationImpl(double left, double right) {
        return left - right;
    }
}