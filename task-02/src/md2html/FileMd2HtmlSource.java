package md2html;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FileMd2HtmlSource extends Md2HtmlSource {
    private final Reader reader;

    public FileMd2HtmlSource(final String fileName) throws Md2HtmlException {
        try {
            reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw error("Error opening input file '%s': %s", fileName, e.getMessage());
        }
    }

    @Override
    protected char readChar() throws IOException {
        final int read = reader.read();
        return read == -1 ? END : (char) read;
    }

    public void close() throws IOException {
        reader.close();
    }
}
