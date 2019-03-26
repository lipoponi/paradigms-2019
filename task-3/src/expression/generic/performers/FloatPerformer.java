package expression.generic.performers;

import expression.generic.exceptions.DivisionByZeroException;

public class FloatPerformer extends AbstractPerformer<Float> {
    public FloatPerformer() {
        super(null);
    }

    @Override
    protected Float applyAdd(Float left, Float right) {
        return left + right;
    }

    @Override
    protected Float applySubtract(Float left, Float right) {
        return left - right;
    }

    @Override
    protected Float applyMultiply(Float left, Float right) {
        return left * right;
    }

    @Override
    protected Float applyDivide(Float left, Float right) {
        return left / right;
    }

    @Override
    protected Float applyNegate(Float inner) {
        return -inner;
    }

    @Override
    protected Float applyAbs(Float inner) {
        return inner < 0 ? -inner : inner;
    }

    @Override
    protected Float applySquare(Float inner) {
        return inner * inner;
    }

    @Override
    protected Float applyMod(Float left, Float right) {
        return left % right;
    }

    @Override
    public Float parse(String number) {
        return Float.parseFloat(number);
    }
}
