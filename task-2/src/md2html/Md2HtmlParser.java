package md2html;

public class Md2HtmlParser {
    private final Md2HtmlSource source;

    public Md2HtmlParser(final Md2HtmlSource source) {
        this.source = source;
    }

    public String parse() throws Md2HtmlException {
        source.nextChar();
        skipEmptyLines();
        StringBuilder result = new StringBuilder();

        while (source.getChar() != Md2HtmlSource.END) {
            result.append(parseBlock());
            result.append('\n');
            skipEmptyLines();
        }
        source.finish();

        return result.toString();
    }


    private void skipEmptyLines() throws Md2HtmlException {
        while (source.getChar() == '\n') {
            source.nextChar();
        }
    }

    private boolean testPrevious(char c) throws Md2HtmlException {
        return c == source.getPrev();
    }

    private boolean testCurrent(char c) throws Md2HtmlException {
        return c == source.getChar();
    }

    private boolean testAndStep(char c) throws Md2HtmlException {
        if (c == source.getChar()) {
            source.nextChar();
            return true;
        }
        return false;
    }


    private StringBuilder parseBlock() throws Md2HtmlException {
        StringBuilder result = new StringBuilder();
        String end;

        int headerDegree = 0;
        while (testCurrent('#') && headerDegree < 6) {
            headerDegree++;
            source.nextChar();
        }

        if (Character.isWhitespace(source.getChar()) && headerDegree != 0) {
            result.append("<h").append(headerDegree).append(">");
            end = "</h" + headerDegree + ">";
            source.nextChar();
        } else {
            result.append("<p>");
            end = "</p>";
            for (int i = 0; i < headerDegree; i++) {
                result.append('#');
            }
        }

        while (!(testPrevious('\n') && testCurrent('\n')) && !testCurrent(Md2HtmlSource.END)) {
            result.append(parseContent());
        }

        if (result.charAt(result.length() - 1) == '\n') {
            result.setLength(result.length() - 1);
        }

        return result.append(end);
    }

    private StringBuilder parseContent() throws Md2HtmlException {
        if (testAndStep('+')) {
            if (testAndStep('+')) {
                return parseUnderline();
            }
            return new StringBuilder("+").append(parseContent());
        }
        if (testAndStep('-')) {
            if (testAndStep('-')) {
                return parseStrikeThrough();
            }
            return new StringBuilder("-").append(parseContent());
        }
        if (testAndStep('*')) {
            if (testAndStep('*')) {
                return parseBold('*');
            }

            return parseItalic('*');
        }
        if (testAndStep('_')) {
            if (testAndStep('_')) {
                return parseBold('_');
            }

            return parseItalic('_');
        }
        if (testAndStep('~')) {
            return parseMark();
        }
        if (testAndStep('`')) {
            return parseCode();
        }

        if (testAndStep('\\')) {
            if ("+-*_\\".indexOf(source.getChar()) != -1) {
                char c = source.getChar();
                source.nextChar();
                return new StringBuilder().append(c);
            }
            return new StringBuilder("\\");
        }
        if (testAndStep('<')) {
            return new StringBuilder("&lt;");
        }
        if (testAndStep('>')) {
            return new StringBuilder("&gt;");
        }
        if (testAndStep('&')) {
            return new StringBuilder("&amp;");
        }

        if (testAndStep('[')) {
            return parseLink();
        }
        if (testAndStep(']')) {
            return new StringBuilder("]");
        }
        if (testAndStep('!')) {
            return parseImage();
        }

        return parsePlain();
    }

    private StringBuilder parsePlain() throws Md2HtmlException {
        StringBuilder result = new StringBuilder();
        String forbidden = "*_-`\\[+]~<>&!" + Md2HtmlSource.END;

        while (forbidden.indexOf(source.getChar()) == -1 && !(testPrevious('\n') && testCurrent('\n'))) {
            result.append(source.getChar());
            source.nextChar();
        }

        return result;
    }


    private StringBuilder parseSingleDecorator(char delimiter, String tag) throws Md2HtmlException {
        if (testAndStep(delimiter)) {
            return new StringBuilder();
        }

        StringBuilder buffer = new StringBuilder();

        while (true) {
            if (testAndStep(delimiter)) {
                StringBuilder result = new StringBuilder();
                result.append('<').append(tag).append('>');
                result.append(buffer);
                result.append("</").append(tag).append('>');
                return result;
            }

            if (testCurrent(Md2HtmlSource.END) || (testPrevious('\n') && testCurrent('\n'))) {
                return new StringBuilder().append(delimiter).append(buffer);
            }

            buffer.append(parseContent());
        }
    }

    private StringBuilder parseItalic(char delimiter) throws Md2HtmlException {
        return parseSingleDecorator(delimiter, "em");
    }

    private StringBuilder parseMark() throws Md2HtmlException {
        return parseSingleDecorator('~', "mark");
    }

    private StringBuilder parseCode() throws Md2HtmlException {
        return parseSingleDecorator('`', "code");
    }


    private StringBuilder parseDoubleDecorator(char delimiter, String tag, boolean bold) throws Md2HtmlException {
        StringBuilder buffer = new StringBuilder();
        if (testAndStep(delimiter)) {
            if (testAndStep(delimiter)) {
                return new StringBuilder();
            } else if (bold) {
                buffer.append(parseItalic(delimiter));
            } else{
                buffer.append(delimiter);
                buffer.append(parseContent());
            }
        }
        while (true) {
            if (testAndStep(delimiter)) {
                if (testAndStep(delimiter)) {
                    StringBuilder result = new StringBuilder();
                    result.append('<').append(tag).append('>');
                    result.append(buffer);
                    result.append("</").append(tag).append('>');
                    return result;
                } else {
                    buffer.append(delimiter);
                }
            }
            if (testCurrent(Md2HtmlSource.END) || (testCurrent('\n') && testPrevious('\n'))) {
                StringBuilder result = new StringBuilder();
                result.append(delimiter).append(delimiter);
                return result.append(buffer);
            }
            buffer.append(parseContent());
        }
    }

    private StringBuilder parseUnderline() throws Md2HtmlException {
        return parseDoubleDecorator('+', "u", false);
    }

    private StringBuilder parseStrikeThrough() throws Md2HtmlException {
        return parseDoubleDecorator('-', "s", false);
    }

    private StringBuilder parseBold(char delimiter) throws Md2HtmlException {
        return parseDoubleDecorator(delimiter, "strong", true);
    }


    private StringBuilder parseLink() throws Md2HtmlException {
        StringBuilder text = parseInner(']', false);
        if (testCurrent(Md2HtmlSource.END) || testCurrent('\n')) {
            return new StringBuilder("[").append(text);
        }
        source.nextChar();

        if (testAndStep('(')) {
            StringBuilder link = parseInner(')', true);
            if (testCurrent(Md2HtmlSource.END) || testCurrent('\n')) {
                return new StringBuilder("[").append(text).append("](").append(link);
            }
            source.nextChar();

            StringBuilder result = new StringBuilder();
            result.append("<a href='").append(link).append("'>");
            result.append(text).append("</a>");
            return result;
        } else {
            return new StringBuilder("[").append(text).append("]");
        }
    }

    private StringBuilder parseImage() throws Md2HtmlException {
        if (testAndStep('[')) {
            StringBuilder text = parseInner(']', true);
            if (testCurrent(Md2HtmlSource.END) || testCurrent('\n')) {
                return new StringBuilder("![").append(text);
            }
            source.nextChar();

            if (testAndStep('(')) {
                StringBuilder link = parseInner(')', true);
                if (testCurrent(Md2HtmlSource.END) || testCurrent('\n')) {
                    return new StringBuilder("![").append(text).append("](").append(link);
                }
                source.nextChar();

                StringBuilder result = new StringBuilder();
                result.append("<img alt='").append(text).append("' src='");
                result.append(link).append("'>");
                return result;
            } else {
                return new StringBuilder("![").append(text).append("]");
            }
        } else {
            return new StringBuilder("!");
        }
    }

    private StringBuilder parseInner(char ending, boolean plain) throws Md2HtmlException {
        if (testCurrent(ending)) {
            return new StringBuilder();
        }

        StringBuilder buffer = new StringBuilder();
        while (true) {
            if (testCurrent(Md2HtmlSource.END) || testPrevious('\n') && testCurrent('\n') || testCurrent(ending)) {
                return buffer;
            }

            if (plain) {
                buffer.append(source.getChar());
                source.nextChar();
            } else {
                buffer.append(parseContent());
            }
        }
    }
}
