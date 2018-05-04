package magic.data;

public class RomanToInt {

    public static int convert(String num) {
        // Only support 1-3 for now since Sagas only have these numbers
        switch (num) {
            case "I": return 1;
            case "II": return 2;
            case "III": return 3;
            default: throw new RuntimeException("unknown roman numeral \"" + num + "\"");
        }
    }
}
