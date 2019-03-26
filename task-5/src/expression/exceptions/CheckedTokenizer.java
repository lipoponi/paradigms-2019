package expression.exceptions;

import expression.exceptions.ownExceptions.ParseException;
import expression.exceptions.ownExceptions.ConstOverflowException;
import expression.exceptions.ownExceptions.UnknownIdentifierException;
import expression.exceptions.ownExceptions.UnsupportedSymbolException;

class CheckedTokenizer {
    private final String expression;
    private int index = 0;
    private int lastPosition = 0;
    private Token lastToken = null;
    private Integer lastNumber = null;
    private String lastIdentifier = null;

    private void skipWhitespace() {
        while (index < expression.length() && Character.isWhitespace(expression.charAt(index))) {
            index++;
        }

        lastPosition = index;
    }

    private void readNumber() throws ParseException {
        int result = 0;
        boolean negative = expression.charAt(index) == '-';
        lastPosition = index;
        index += negative ? 1 : 0;

        while (index < expression.length() && Character.isDigit(expression.charAt(index))) {
            int nextValue = result * 10 + (negative ? -1 : 1) * (expression.charAt(index) - '0');

            if ((negative && result < nextValue) || (!negative && nextValue < result)) {
                String number = expression.substring(lastPosition, index + 1);
                int position = lastPosition + 1;
                throw new ConstOverflowException(number, position);
            }

            result = nextValue;
            index++;
        }

        lastNumber = result;
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

    CheckedTokenizer(String expression) {
        assert expression != null;

        this.expression = expression;
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
                        case "sqrt":
                            lastToken = Token.SQRT;
                            break;
                        case "min":
                            lastToken = Token.MIN;
                            break;
                        case "max":
                            lastToken = Token.MAX;
                            break;
                        case "high":
                            lastToken = Token.HIGH;
                            break;
                        case "low":
                            lastToken = Token.LOW;
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

    int getLastNumber() {
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
