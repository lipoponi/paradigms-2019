package expression;

public class BitwiseXor extends BinaryOperator {
    public BitwiseXor(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    protected int operationImpl(int left, int right) {
        return left ^ right;
    }

    protected double operationImpl(double left, double right) {
        return 0;
    }
}
