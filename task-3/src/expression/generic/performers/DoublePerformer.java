package expression.generic.performers;

import expression.generic.exceptions.DivisionByZeroException;

public class DoublePerformer extends AbstractPerformer<Double> {
    public DoublePerformer() {
        super(null);
    }

    @Override
    protected Double applyAdd(Double left, Double right) {
        return left + right;
    }

    @Override
    protected Double applySubtract(Double left, Double right) {
        return left - right;
    }

    @Override
    protected Double applyMultiply(Double left, Double right) {
        return left * right;
    }

    @Override
    protected Double applyDivide(Double left, Double right) {
        return left / right;
    }

    @Override
    protected Double applyNegate(Double inner) {
        return -inner;
    }

    @Override
    protected Double applyAbs(Double inner) {
        return inner < 0 ? -inner : inner;
    }

    @Override
    protected Double applySquare(Double inner) {
        return inner * inner;
    }

    @Override
    protected Double applyMod(Double left, Double right) {
        return left % right;
    }

    @Override
    public Double parse(String number) {
        return Double.parseDouble(number);
    }
}
