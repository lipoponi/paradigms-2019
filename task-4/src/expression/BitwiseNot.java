package expression;

public class BitwiseNot extends UnaryOperator {
    public BitwiseNot(CommonExpression inner) {
        super(inner);
    }

    protected int operationImpl(int x) {
        return ~x;
    }

    protected double operationImpl(double x) {
        return 0;
    }
}
