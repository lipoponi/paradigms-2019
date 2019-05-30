package expression.generic;

import expression.generic.expressions.TripleExpression;
import expression.generic.exceptions.ParseException;
import expression.generic.performers.Performer;

public interface Parser<T extends Number> {
    TripleExpression<T> parse(String expression) throws ParseException;
}
