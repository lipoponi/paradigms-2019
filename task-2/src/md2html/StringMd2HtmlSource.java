package md2html;

import java.io.IOException;

public class StringMd2HtmlSource extends Md2HtmlSource {
    private final String data;

    public StringMd2HtmlSource(final String data) throws Md2HtmlException {
        this.data = data + END;
    }

    @Override
    protected char readChar() {
        return data.charAt(pos);
    }

    @Override
    public void close() throws IOException {

    }
}
