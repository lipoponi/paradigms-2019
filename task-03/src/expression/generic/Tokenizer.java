package expression.generic;

import expression.generic.exceptions.*;
import expression.generic.performers.Performer;

public class Tokenizer<T extends Number> {
    private final String expression;
    private final Performer<T> performer;
    private int index = 0;
    private int lastPosition = 0;
    private Token lastToken = null;
    private T lastNumber = null;
    private String lastIdentifier = null;

    private void skipWhitespace() {
        while (index < expression.length() && Character.isWhitespace(expression.charAt(index))) {
            index++;
        }

        lastPosition = index;
    }

    private T createNumber(Class<T> clazz, String number) {
        return clazz.cast(Integer.parseInt(number));
    }

    private void readNumber() throws ParseException {
        StringBuilder numberBuffer = new StringBuilder();
        lastPosition = index;
        if (expression.charAt(index) == '-') {
            numberBuffer.append('-');
            index++;
        }

        while (index < expression.length() && Character.isDigit(expression.charAt(index))) {
            numberBuffer.append(expression.charAt(index++));
        }

        String number = numberBuffer.toString();

        lastNumber = performer.parse(number);
    }

    private void readIdentifier() {
        StringBuilder result = new StringBuilder();
        lastPosition = index;

        while (index < expression.length() && Character.isLetterOrDigit(expression.charAt(index))) {
            result.append(expression.charAt(index));
            index++;
        }

        lastIdentifier = result.toString();
    }

    Tokenizer(String expression, Performer<T> performer) {
        assert expression != null;

        this.expression = expression;
        this.performer = performer;
    }

    void nextToken(boolean isPrimary) throws ParseException {
        skipWhitespace();

        if (expression.length() <= index) {
            lastToken = Token.END;
            return;
        }

        lastPosition = index;
        char currentCharacter = expression.charAt(index++);

        switch (currentCharacter) {
            case '(':
                lastToken = Token.LP;
                break;
            case ')':
                lastToken = Token.RP;
                break;
            case '+':
                lastToken = Token.PLUS;
                break;
            case '-':
                if (isPrimary && index < expression.length() && Character.isDigit(expression.charAt(index))) {
                    lastToken = Token.NUMBER;
                    index--;
                    readNumber();
                } else {
                    lastToken = Token.MINUS;
                }
                break;
            case '*':
                lastToken = Token.MUL;
                break;
            case '/':
                lastToken = Token.DIV;
                break;
            default:
                if (Character.isDigit(currentCharacter)) {
                    index--;
                    readNumber();
                    lastToken = Token.NUMBER;
                } else if (Character.isLetter(currentCharacter)) {
                    index--;
                    readIdentifier();

                    switch (lastIdentifier) {
                        case "abs":
                            lastToken = Token.ABS;
                            break;
                        case "square":
                            lastToken = Token.SQR;
                            break;
                        case "mod":
                            lastToken = Token.MOD;
                            break;
                        case "x":
                        case "y":
                        case "z":
                            lastToken = Token.VARIABLE;
                            break;
                        default:
                            int position = lastPosition + 1;
                            throw new UnknownIdentifierException(lastIdentifier, position);
                    }
                } else {
                    throw new UnsupportedSymbolException(currentCharacter, index);
                }
        }
    }

    void nextToken() throws ParseException {
        nextToken(false);
    }

    Token getLastToken() {
        assert lastToken != null;

        return lastToken;
    }

    T getLastNumber() {
        assert lastNumber != null;

        return lastNumber;
    }

    String getLastIdentifier() {
        assert lastIdentifier != null;

        return lastIdentifier;
    }

    int getLastPosition() {
        return lastPosition;
    }
}
