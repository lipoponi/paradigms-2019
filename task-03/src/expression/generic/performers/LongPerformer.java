package expression.generic.performers;

import expression.generic.exceptions.DivisionByZeroException;

public class LongPerformer extends AbstractPerformer<Long> {
    public LongPerformer() {
        super(null);
    }

    @Override
    protected Long applyAdd(Long left, Long right) {
        return left + right;
    }

    @Override
    protected Long applySubtract(Long left, Long right) {
        return left - right;
    }

    @Override
    protected Long applyMultiply(Long left, Long right) {
        return left * right;
    }

    @Override
    protected Long applyDivide(Long left, Long right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        return left / right;
    }

    @Override
    protected Long applyNegate(Long inner) {
        return -inner;
    }

    @Override
    protected Long applyAbs(Long inner) {
        return inner < 0 ? -inner : inner;
    }

    @Override
    protected Long applySquare(Long inner) {
        return inner * inner;
    }

    @Override
    protected Long applyMod(Long left, Long right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        return left % right;
    }

    @Override
    public Long parse(String number) {
        return Long.parseLong(number);
    }
}
