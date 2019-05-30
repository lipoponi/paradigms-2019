package md2html;

import java.io.IOException;

public abstract class Md2HtmlSource {
    public static char END = '\0';

    protected int pos;
    protected int line = 1;
    protected int posInLine;
    private char c;
    private char prev;

    protected abstract char readChar() throws IOException;

    public abstract void close() throws IOException;

    public char getChar() {
        return c;
    }

    public char getPrev() {
        return prev;
    }

    public char nextChar() throws Md2HtmlException {
        try {
            if (c == '\n') {
                line++;
                posInLine = 0;
            }
            prev = c;
            do {
                c = readChar();
                pos++;
                posInLine++;
            } while (c == '\r');
            return c;
        } catch (final IOException e) {
            throw error("Source read error", e.getMessage());
        }
    }

    public Md2HtmlException error(final String format, final Object... args) {
        return new Md2HtmlException(line, posInLine, String.format("%d:%d: %s", line, posInLine, String.format(format, args)));
    }

    public void finish() throws Md2HtmlException {
        try {
            this.close();
        } catch (IOException e) {
            throw error("File close error", e.getMessage());
        }
    }
}
