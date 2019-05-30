package expression.generic.performers;

import expression.generic.exceptions.DivisionByZeroException;
import expression.generic.exceptions.ModulusNotPositiveException;

import java.math.BigInteger;

public class BigIntegerPerformer extends AbstractPerformer<BigInteger> {
    public BigIntegerPerformer() {
        super(null);
    }

    @Override
    protected BigInteger applyAdd(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    protected BigInteger applySubtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    protected BigInteger applyMultiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    protected BigInteger applyDivide(BigInteger left, BigInteger right) {
        if (right.signum() == 0) {
            throw new DivisionByZeroException();
        }
        return left.divide(right);
    }

    @Override
    protected BigInteger applyNegate(BigInteger inner) {
        return inner.negate();
    }

    @Override
    protected BigInteger applyAbs(BigInteger inner) {
        return inner.abs();
    }

    @Override
    protected BigInteger applySquare(BigInteger inner) {
        return inner.pow(2);
    }

    @Override
    protected BigInteger applyMod(BigInteger left, BigInteger right) {
        if (right.signum() <= 0) {
            throw new ModulusNotPositiveException();
        }
        return left.mod(right);
    }

    @Override
    public BigInteger parse(String number) {
        return new BigInteger(number);
    }
}
