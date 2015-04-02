package magic.model;

import magic.data.EnglishToInt;

import java.util.regex.Matcher;

public class ARG {  
    public static final String NUMBER = "(?<number>[0-9]+)";
    public static int number(final Matcher m) {
        return Integer.parseInt(m.group("number"));
    }
    
    public static final String AMOUNT = "(?<amount>[^ ]+)";
    public static int amount(final Matcher m) {
        return EnglishToInt.convert(m.group("amount"));
    }
    
    public static final String COST = "(?<cost>.+)";
    public static String cost(final Matcher m) {
        return m.group("cost");
    }
    
    public static final String EFFECT = "(?<effect>.+)";
    public static String effect(final Matcher m) {
        return m.group("effect");
    }

    public static final String ANY = "(?<any>.+)";
    public static String any(final Matcher m) {
        return m.group("any");
    }

    public static final String MANA = "(?<mana>[^\\.]+)";
    public static String mana(final Matcher m) {
        return m.group("mana");
    }
    
    public static final String MANACOST = "(?<manacost>(\\{[A-Z\\d/]+\\})+)";
    public static String manacost(final Matcher m) {
        return m.group("manacost");
    }
    
    public static final String WORD1 = "(?<word1>[^ ]+)";
    public static String word1(final Matcher m) {
        return m.group("word1");
    }
    
    public static final String WORD2 = "(?<word2>[^ ]+)";
    public static String word2(final Matcher m) {
        return m.group("word2");
    }
    
    public static final String WORDRUN = "(?<wordrun>[^\\.\"]+)";
    public static String wordrun(final Matcher m) {
        return m.group("wordrun");
    }

    public static final String WORDRUN2 = "(?<wordrun2>[^\\.\"]+)";
    public static String wordrun2(final Matcher m) {
        return m.group("wordrun2");
    }
    
    public static final String PT = "(?<pt>[+-][0-9]+/[+-][0-9]+)";
    public static String pt(final Matcher m) {
        return m.group("pt");
    }
    
    public static final String IT = "(?<it>(rn|sn|it|this permanent|this creature))";
    public static String it(final Matcher m) {
        return m.group("it");
    }
    
    public static final String YOU = "(?<you>(rn|pn|you||))";
    public static String you(final Matcher m) {
        return m.group("you");
    }
    
    public static final String COLON = "\\s*:\\s*";
} 
