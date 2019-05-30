package expression.parser;

class Tokenizer {
    private String expression;
    private int index = 0;
    private Token lastToken;
    private int lastNumber;
    private String lastIdentifier;

    private void skipWhitespace() {
        while (index < expression.length() && Character.isWhitespace(expression.charAt(index))) {
            index++;
        }
    }

    private void readNumber() {
        int result = 0;

        while (index < expression.length() && Character.isDigit(expression.charAt(index))) {
            result = result * 10 + (expression.charAt(index) - '0');
            index++;
        }

        lastNumber = result;
    }

    private void readIdentifier() {
        StringBuilder result = new StringBuilder();

        while (index < expression.length() && Character.isLetterOrDigit(expression.charAt(index))) {
            result.append(expression.charAt(index));
            index++;
        }

        lastIdentifier = result.toString();
    }

    Tokenizer(String expression) {
        this.expression = expression;
    }

    void nextToken() {
        skipWhitespace();

        if (expression.length() <= index) {
            lastToken = Token.END;
            return;
        }

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
                lastToken = Token.MINUS;
                break;
            case '*':
                lastToken = Token.MUL;
                break;
            case '/':
                lastToken = Token.DIV;
                break;
            case '&':
                lastToken = Token.BW_AND;
                break;
            case '^':
                lastToken = Token.BW_XOR;
                break;
            case '|':
                lastToken = Token.BW_OR;
                break;
            case '~':
                lastToken = Token.BW_NOT;
                break;
            default:
                if (Character.isDigit(currentCharacter)) {
                    index--;
                    readNumber();
                    lastToken = Token.NUMBER;
                } else if (Character.isLetter(currentCharacter)) {
                    index--;
                    readIdentifier();

                    if (lastIdentifier.equals("count")) {
                        lastToken = Token.BW_COUNT;
                    } else {
                        lastToken = Token.VARIABLE;
                    }
                } else {
                    lastToken = null;
                }
        }
    }

    Token getLastToken() {
        return lastToken;
    }

    int getLastNumber() {
        return lastNumber;
    }

    String getLastIdentifier() {
        return lastIdentifier;
    }
}
