package expression.parser;

import expression.*;

public class ExpressionParser implements Parser {
    private Tokenizer tokenizer;

    private CommonExpression primary() {
        CommonExpression result = null;
        tokenizer.nextToken();

        switch (tokenizer.getLastToken()) {
            case NUMBER:
                result = new Const(tokenizer.getLastNumber());
                tokenizer.nextToken();
                break;
            case VARIABLE:
                result = new Variable(tokenizer.getLastIdentifier());
                tokenizer.nextToken();
                break;
            case LP:
                result = startPoint();
                tokenizer.nextToken();
                break;
            case MINUS:
                result = new Negate(primary());
                break;
            case BW_NOT:
                result = new BitwiseNot(primary());
                break;
            case BW_COUNT:
                result = new BitCount(primary());
                break;
        }

        return result;
    }

    private CommonExpression mulDiv() {
        CommonExpression result = primary();

        while (true) {
            switch (tokenizer.getLastToken()) {
                case MUL:
                    result = new Multiply(result, primary());
                    break;
                case DIV:
                    result = new Divide(result, primary());
                    break;
                default:
                    return result;
            }
        }
    }

    private CommonExpression addSub() {
        CommonExpression result = mulDiv();

        while (true) {
            switch (tokenizer.getLastToken()) {
                case PLUS:
                    result = new Add(result, mulDiv());
                    break;
                case MINUS:
                    result = new Subtract(result, mulDiv());
                    break;
                default:
                    return result;
            }
        }
    }

    private CommonExpression bitwiseAnd() {
        CommonExpression result = addSub();

        while (tokenizer.getLastToken() == Token.BW_AND) {
            result = new BitwiseAnd(result, addSub());
        }

        return result;
    }

    private CommonExpression bitwiseXor() {
        CommonExpression result = bitwiseAnd();

        while (tokenizer.getLastToken() == Token.BW_XOR) {
            result = new BitwiseXor(result, bitwiseAnd());
        }

        return result;
    }

    private CommonExpression bitwiseOr() {
        CommonExpression result = bitwiseXor();

        while (tokenizer.getLastToken() == Token.BW_OR) {
            result = new BitwiseOr(result, bitwiseXor());
        }

        return result;
    }

    private CommonExpression startPoint() {
        return bitwiseOr();
    }

    public CommonExpression parse(String expression) {
        this.tokenizer = new Tokenizer(expression);

        return startPoint();
    }
}