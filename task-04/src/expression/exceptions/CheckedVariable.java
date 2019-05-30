package expression.exceptions;

import expression.TripleExpression;
import expression.exceptions.ownExceptions.UnknownIdentifierException;
import expression.exceptions.ownExceptions.ParseException;

public class CheckedVariable implements TripleExpression {
    private final String name;

    public CheckedVariable(String name) throws ParseException {
        if (!"xyz".contains(name)) {
            throw new UnknownIdentifierException(name);
        }

        this.name = name;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (this.name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }

        return 0;
    }
}
