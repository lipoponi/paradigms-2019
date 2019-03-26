package expression.generic.performers;

import expression.generic.exceptions.DivisionByZeroException;

public class BytePerformer extends AbstractPerformer<Byte> {
    public BytePerformer() {
        super(null);
    }

    @Override
    protected Byte applyAdd(Byte left, Byte right) {
        return ((Integer)(left + right)).byteValue();
    }

    @Override
    protected Byte applySubtract(Byte left, Byte right) {
        return ((Integer)(left - right)).byteValue();
    }

    @Override
    protected Byte applyMultiply(Byte left, Byte right) {
        return ((Integer)(left * right)).byteValue();
    }

    @Override
    protected Byte applyDivide(Byte left, Byte right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        return ((Integer)(left / right)).byteValue();
    }

    @Override
    protected Byte applyNegate(Byte inner) {
        return ((Integer)(-inner)).byteValue();
    }

    @Override
    protected Byte applyAbs(Byte inner) {
        return inner < 0 ? ((Integer)(-inner)).byteValue() : inner;
    }

    @Override
    protected Byte applySquare(Byte inner) {
        return ((Integer)(inner * inner)).byteValue();
    }

    @Override
    protected Byte applyMod(Byte left, Byte right) {
        if (right == 0) {
            throw new DivisionByZeroException();
        }
        return ((Integer)(left % right)).byteValue();
    }

    @Override
    public Byte parse(String number) {
        return ((Integer)Integer.parseInt(number)).byteValue();
    }
}
