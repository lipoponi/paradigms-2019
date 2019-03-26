package expression.generic.checkers;

import expression.generic.exceptions.*;

public class IntegerPerformerChecker implements Checker<Integer> {
    @Override
    public void checkAdd(Integer left, Integer right) {
        if (right < 0 && left < left + right || 0 < right && left + right < left) {
            throw new OverflowException("+", left, right);
        }
    }

    @Override
    public void checkSubtract(Integer left, Integer right) {
        if (right < 0 && left - right < left || 0 < right && left < left - right) {
            throw new OverflowException("-", left, right);
        }
    }

    @Override
    public void checkMultiply(Integer left, Integer right) {
        int x = left * right;
        int min = (left < right) ? left : right;
        int max = (left < right) ? right : left;

        if (left != 0 && x / left != right || min == Integer.MIN_VALUE && max == -1) {
            throw new OverflowException("*", left, right);
        }
    }

    @Override
    public void checkDivide(Integer left, Integer right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }

        if (right == -1 && left == Integer.MIN_VALUE) {
            throw new OverflowException("/", left, right);
        }
    }

    @Override
    public void checkNegate(Integer inner) {
        if (inner == Integer.MIN_VALUE) {
            throw new OverflowException("-", inner);
        }
    }

    @Override
    public void checkAbs(Integer inner) {
        if (inner == Integer.MIN_VALUE) {
            throw new OverflowException("abs", inner);
        }
    }

    @Override
    public void checkSquare(Integer inner) {
        int x = inner * inner;

        if (inner != 0 && x / inner != inner) {
            throw new OverflowException("square", inner);
        }
    }

    @Override
    public void checkMod(Integer left, Integer right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }

        if (right == -1 && left == Integer.MIN_VALUE) {
            throw new OverflowException("/", left, right);
        }
    }
}
