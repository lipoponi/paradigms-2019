package expression.generic.expressions;

import expression.generic.exceptions.*;

public class Variable<T extends Number> implements TripleExpression<T> {
    private final String name;

    public Variable(String name) throws ParseException {
        if (!"xyz".contains(name)) {
            throw new UnknownIdentifierException(name);
        }

        this.name = name;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        switch (this.name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                return null;
        }
    }
}
