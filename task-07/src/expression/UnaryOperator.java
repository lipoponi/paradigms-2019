package expression;

public abstract class UnaryOperator implements CommonExpression {
    protected final CommonExpression inner;

    protected abstract int operationImpl(int value);
    protected abstract double operationImpl(double value);

    public UnaryOperator(CommonExpression inner) {
        this.inner = inner;
    }

    public int evaluate(int x) {
        return operationImpl(inner.evaluate(x));
    }

    public double evaluate(double x) {
        return operationImpl(inner.evaluate(x));
    }

    public int evaluate(int x, int y, int z) {
        return operationImpl(inner.evaluate(x, y, z));
    }
}