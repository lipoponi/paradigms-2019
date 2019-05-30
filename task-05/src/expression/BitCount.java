package expression;

public class BitCount extends UnaryOperator {
    public BitCount(CommonExpression inner) {
        super(inner);
    }

    protected int operationImpl(int x) {
        return Integer.bitCount(x);
    }

    protected double operationImpl(double x) {
        return 0;
    }
}
