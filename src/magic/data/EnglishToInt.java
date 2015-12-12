package magic.data;

public class EnglishToInt {

    public static int convert(String num) {
        if (num == null) {
            return 1;
        }
        switch (num) {
            case "no": return 0;
            case "a": return 1;
            case "an": return 1;
            case "one": return 1;
            case "two": return 2;
            case "three" : return 3;
            case "four" : return 4;
            case "five" : return 5;
            case "six" : return 6;
            case "seven" : return 7;
            case "eight" : return 8;
            case "nine" : return 9;
            case "ten" : return 10;
            case "eleven" : return 11;
            case "twelve" : return 12;
            case "thirteen" : return 13;
            case "fourteen" : return 14;
            case "fifteen" : return 15;
            case "twenty" : return 20;
            case "ninety-nine": return 99;
            case "100": return 100;
            default: throw new RuntimeException("unknown count \"" + num + "\"");
        }
    }
}
