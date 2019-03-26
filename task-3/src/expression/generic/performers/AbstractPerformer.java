package expression.generic.performers;

import expression.generic.checkers.Checker;

public abstract class AbstractPerformer<T extends Number> implements Performer<T> {
    private final boolean checked;
    private final Checker<T> checker;


    protected abstract T applyAdd(T left, T right);

    protected abstract T applySubtract(T left, T right);

    protected abstract T applyMultiply(T left, T right);

    protected abstract T applyDivide(T left, T right);

    protected abstract T applyNegate(T inner);

    protected abstract T applyAbs(T inner);

    protected abstract T applySquare(T inner);

    protected abstract T applyMod(T left, T right);


    public AbstractPerformer(Checker<T> checker) {
        this.checked = checker != null;
        this.checker = checker;
    }


    @Override
    public T add(T left, T right) {
        if (checked) {
            checker.checkAdd(left, right);
        }
        return applyAdd(left, right);
    }

    @Override
    public T subtract(T left, T right) {
        if (checked) {
            checker.checkSubtract(left, right);
        }
        return applySubtract(left, right);
    }

    @Override
    public T multiply(T left, T right) {
        if (checked) {
            checker.checkMultiply(left, right);
        }
        return applyMultiply(left, right);
    }

    @Override
    public T divide(T left, T right) {
        if (checked) {
            checker.checkDivide(left, right);
        }
        return applyDivide(left, right);
    }

    @Override
    public T negate(T inner) {
        if (checked) {
            checker.checkNegate(inner);
        }
        return applyNegate(inner);
    }

    @Override
    public T abs(T inner) {
        if (checked) {
            checker.checkAbs(inner);
        }
        return applyAbs(inner);
    }

    @Override
    public T square(T inner) {
        if (checked) {
            checker.checkSquare(inner);
        }
        return applySquare(inner);
    }

    @Override
    public T mod(T left, T right) {
        if (checked) {
            checker.checkMod(left, right);
        }
        return applyMod(left, right);
    }
}
