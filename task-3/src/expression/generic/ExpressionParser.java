package expression.generic;

import expression.generic.exceptions.*;
import expression.generic.expressions.*;
import expression.generic.performers.Performer;

public class ExpressionParser<T extends Number> implements Parser<T> {
    private final Performer<T> performer;
    private Tokenizer<T> tokenizer;

    public ExpressionParser(Performer<T> performer) {
        this.performer = performer;
    }

    private TripleExpression<T> primary() throws ParseException {
        TripleExpression<T> result;
        tokenizer.nextToken(true);
        int position = tokenizer.getLastPosition() + 1;

        switch (tokenizer.getLastToken()) {
            case NUMBER:
                result = new Const<>(tokenizer.getLastNumber());
                tokenizer.nextToken();
                break;
            case VARIABLE:
                String identifier = tokenizer.getLastIdentifier();

                if (!"xyz".contains(identifier)) {
                    throw new UnknownIdentifierException(identifier);
                }

                result = new Variable<>(identifier);
                tokenizer.nextToken();
                break;
            case LP:
                result = startPoint();

                if (tokenizer.getLastToken() != Token.RP) {
                    String token = tokenizer.getLastToken().toString();
                    throw new UnexpectedTokenException(token, position);
                }

                tokenizer.nextToken();
                break;
            case MINUS:
                result = new Negate<>(performer, primary());
                break;
            case ABS:
                result = new Abs<>(performer, primary());
                break;
            case SQR:
                result = new Square<>(performer, primary());
                break;
            default:
                String token = tokenizer.getLastToken().toString();
                throw new UnexpectedTokenException(token, position);
        }

        return result;
    }

    private TripleExpression<T> mulDivMod() throws ParseException {
        TripleExpression<T> result = primary();

        while (true) {
            switch (tokenizer.getLastToken()) {
                case MUL:
                    result = new Multiply<>(performer, result, primary());
                    break;
                case DIV:
                    result = new Divide<>(performer, result, primary());
                    break;
                case MOD:
                    result = new Mod<>(performer, result, primary());
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression<T> addSub() throws ParseException {
        TripleExpression<T> result = mulDivMod();

        while (true) {
            switch (tokenizer.getLastToken()) {
                case PLUS:
                    result = new Add<>(performer, result, mulDivMod());
                    break;
                case MINUS:
                    result = new Subtract<>(performer, result, mulDivMod());
                    break;
                default:
                    return result;
            }
        }
    }

    private TripleExpression<T> startPoint() throws ParseException {
        return addSub();
    }

    public TripleExpression<T> parse(String expression) throws ParseException {
        this.tokenizer = new Tokenizer<>(expression, performer);

        TripleExpression<T> result = startPoint();
        if (tokenizer.getLastToken() != Token.END) {
            String token = tokenizer.getLastToken().toString();
            int position = tokenizer.getLastPosition() + 1;
            throw new UnexpectedTokenException(token, position);
        }

        return result;
    }
}
