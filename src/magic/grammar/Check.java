package magic.grammar;

import magic.grammar.SourceString;
import java.util.Scanner;

class Check {
    public static void main(String argv[]) throws Exception {
        final Scanner sc = new Scanner(System.in);
        int parsed = 0;
        int failed = 0;
        MagicRuleParser parser = new MagicRuleParser();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().toLowerCase();
            SourceString src = new SourceString(line);
            boolean ok = parser.parse(src);
            if (ok) {
                System.out.println("PARSED: " + line);
                System.out.println("MSTREE: " + parser.semantics().tree);
                parsed++;
            } else {
                System.out.println("FAILED: " + line);
                failed++;
            }
        }
        System.out.println("Parsed: " + parsed);
        System.out.println("Failed: " + failed);
    }
}
