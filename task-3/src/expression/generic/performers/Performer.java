package expression.generic.performers;

import expression.generic.exceptions.ParseException;

public interface Performer<T extends Number> {
    T add(T left, T right);

    T subtract(T left, T right);

    T multiply(T left, T right);

    T divide(T left, T right);

    T negate(T inner);

    T abs(T inner);

    T square(T inner);

    T mod(T left, T right);

    T parse(String number) throws ParseException;
}
