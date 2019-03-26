package expression;

public class Multiply extends BinaryOperator {
    public Multiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    protected int operationImpl(int left, int right) {
        return left * right;
    }

    protected double operationImpl(double left, double right) {
        return left * right;
    }
}