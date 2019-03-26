package expression.generic.performers;

import expression.generic.checkers.IntegerPerformerChecker;
import expression.generic.exceptions.DivisionByZeroException;

public class IntegerPerformer extends AbstractPerformer<Integer> {
    public IntegerPerformer() {
        super(null);
    }

    public IntegerPerformer(boolean checked) {
        super(checked ? new IntegerPerformerChecker() : null);
    }

    @Override
    protected Integer applyAdd(Integer left, Integer right) {
        return left + right;
    }

    @Override
    protected Integer applySubtract(Integer left, Integer right) {
        return left - right;
    }

    @Override
    protected Integer applyMultiply(Integer left, Integer right) {
        return left * right;
    }

    @Override
    protected Integer applyDivide(Integer left, Integer right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        return left / right;
    }

    @Override
    protected Integer applyNegate(Integer inner) {
        return -inner;
    }

    @Override
    protected Integer applyAbs(Integer inner) {
        return inner < 0 ? -inner : inner;
    }

    @Override
    protected Integer applySquare(Integer inner) {
        return inner * inner;
    }

    @Override
    protected Integer applyMod(Integer left, Integer right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        return left % right;
    }

    @Override
    public Integer parse(String number) {
        return Integer.parseInt(number);
    }
}
