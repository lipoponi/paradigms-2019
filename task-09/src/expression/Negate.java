package expression;

public class Negate extends UnaryOperator {
    public Negate(CommonExpression inner) {
        super(inner);
    }

    protected int operationImpl(int x) {
        return -x;
    }

    protected double operationImpl(double x) {
        return -x;
    }
}