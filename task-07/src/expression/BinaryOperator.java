package expression;

public abstract class BinaryOperator implements CommonExpression {
    protected final CommonExpression left, right;

    protected abstract int operationImpl(int left, int right);
    protected abstract double operationImpl(double left, double right);

    public BinaryOperator(CommonExpression left, CommonExpression right) {
        this.left = left;
        this.right = right;
    }

    public int evaluate(int x) {
        return operationImpl(left.evaluate(x), right.evaluate(x));
    }

    public double evaluate(double x) {
        return operationImpl(left.evaluate(x), right.evaluate(x));
    }

    public int evaluate(int x, int y, int z) {
        return operationImpl(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}