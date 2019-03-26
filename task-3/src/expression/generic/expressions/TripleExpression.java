package expression.generic.expressions;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression<T extends Number> {
    T evaluate(T x, T y, T z);
}
