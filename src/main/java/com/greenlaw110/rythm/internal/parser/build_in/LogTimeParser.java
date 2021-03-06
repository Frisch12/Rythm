package com.greenlaw110.rythm.internal.parser.build_in;

import com.greenlaw110.rythm.exception.ParseException;
import com.greenlaw110.rythm.internal.Keyword;
import com.greenlaw110.rythm.internal.TemplateParser;
import com.greenlaw110.rythm.internal.dialect.Rythm;
import com.greenlaw110.rythm.internal.parser.Directive;
import com.greenlaw110.rythm.internal.parser.ParserBase;
import com.greenlaw110.rythm.spi.IContext;
import com.greenlaw110.rythm.spi.IParser;
import com.greenlaw110.rythm.spi.Token;
import com.greenlaw110.rythm.utils.TextBuilder;
import com.stevesoft.pat.Regex;

public class LogTimeParser extends KeywordParserFactory {

    @Override
    public Keyword keyword() {
        return Keyword.LOG_TIME;
    }

    public IParser create(final IContext ctx) {
        return new ParserBase(ctx) {
            public TextBuilder go() {
                Regex r = reg(dialect());
                if (!r.search(remain())) {
                    throw new ParseException(ctx().getTemplateClass(), ctx().currentLine(), "error parsing @debug, correct usage: @debug(\"msg\", args...)");
                }
                step(r.stringMatched().length());
                return new Directive("", ctx()) {
                    @Override
                    public void call() {
                        ctx().getCodeBuilder().setLogTime();
                    }
                };
            }
        };
    }

    @Override
    protected String patternStr() {
        return "%s%s\\s*\\(\\s*\\)[\\r\\n]+";
    }

    public static void main(String[] args) {
        String s = "@__logTime__()\nabc";
        LogTimeParser ap = new LogTimeParser();
        Regex r = ap.reg(new Rythm());
        if (r.search(s)) {
            System.out.println("m: " + r.stringMatched());
            System.out.println("1: " + r.stringMatched(1));
            System.out.println("2: " + r.stringMatched(2));
            System.out.println("3: " + r.stringMatched(3));
            System.out.println("4: " + r.stringMatched(4));
            System.out.println("5: " + r.stringMatched(5));

            s = r.stringMatched(1);
            if (null == s) return;
            if (s.startsWith("(")) {
                s = s.substring(1);
                s = s.substring(0, s.length() - 1);
            }
            if (s.startsWith("\"") || s.startsWith("'")) {
                s = s.substring(1);
            }
            if (s.endsWith("\"") || s.endsWith("'")) {
                s = s.substring(0, s.length() -1);
            }
            System.out.println(s);
        }
    }
}
