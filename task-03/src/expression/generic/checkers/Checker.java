package expression.generic.checkers;

public interface Checker<T extends Number> {
    void checkAdd(T left, T right);

    void checkSubtract(T left, T right);

    void checkMultiply(T left, T right);

    void checkDivide(T left, T right);

    void checkNegate(T inner);

    void checkAbs(T inner);

    void checkSquare(T inner);

    void checkMod(T left, T right);
}
