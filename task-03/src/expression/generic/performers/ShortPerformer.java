package expression.generic.performers;

import expression.generic.exceptions.DivisionByZeroException;

public class ShortPerformer extends AbstractPerformer<Short> {
    public ShortPerformer() {
        super(null);
    }

    @Override
    protected Short applyAdd(Short left, Short right) {
        return ((Integer)(left + right)).shortValue();
    }

    @Override
    protected Short applySubtract(Short left, Short right) {
        return ((Integer)(left - right)).shortValue();
    }

    @Override
    protected Short applyMultiply(Short left, Short right) {
        return ((Integer)(left * right)).shortValue();
    }

    @Override
    protected Short applyDivide(Short left, Short right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        return ((Integer)(left / right)).shortValue();
    }

    @Override
    protected Short applyNegate(Short inner) {
        return ((Integer)(-inner)).shortValue();
    }

    @Override
    protected Short applyAbs(Short inner) {
        return inner < 0 ? ((Integer)(-inner)).shortValue() : inner;
    }

    @Override
    protected Short applySquare(Short inner) {
        return ((Integer)(inner * inner)).shortValue();
    }

    @Override
    protected Short applyMod(Short left, Short right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        return ((Integer)(left % right)).shortValue();
    }

    @Override
    public Short parse(String number) {
        return ((Integer)Integer.parseInt(number)).shortValue();
    }
}
