package expression.generic;

import expression.generic.exceptions.EvaluationException;
import expression.generic.exceptions.ParseException;
import expression.generic.expressions.TripleExpression;
import expression.generic.performers.*;
import expression.generic.performers.Performer;

public class GenericTabulator implements Tabulator {
    private Performer<?> getPerformer(String mode) {
        switch (mode) {
            case "i":
                return new IntegerPerformer(true);
            case "d":
                return new DoublePerformer();
            case "bi":
                return new BigIntegerPerformer();
            case "u":
                return new IntegerPerformer();
            case "f":
                return new FloatPerformer();
            case "b":
                return new BytePerformer();
            case "l":
                return new LongPerformer();
            case "s":
                return new ShortPerformer();
            default:
                return null;
        }
    }

    public <T extends Number> Object[][][] getTable(Performer<T> performer, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception{
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];

        Parser<T> parser = new ExpressionParser<T>(performer);
        TripleExpression<T> parsed = parser.parse(expression);

        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        result[i][j][k] = parsed.evaluate(
                                performer.parse(Integer.toString(x1 + i)),
                                performer.parse(Integer.toString(y1 + j)),
                                performer.parse(Integer.toString(z1 + k))
                        );
                    } catch (EvaluationException e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        return getTable(getPerformer(mode), expression, x1, x2, y1, y2, z1, z2);
    }
}
