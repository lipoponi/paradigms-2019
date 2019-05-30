package md2html;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Md2Html {
    public static void main(String[] args) {
        String result = "";

        try {
            Md2HtmlSource source = new FileMd2HtmlSource(args[0]);
            Md2HtmlParser parser = new Md2HtmlParser(source);
            result = parser.parse();
        } catch (Md2HtmlException e) {
            e.printStackTrace();
        }

        try (
                FileOutputStream fos = new FileOutputStream(args[1]);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)
        ) {
            osw.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
