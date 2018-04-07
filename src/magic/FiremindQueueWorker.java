package magic;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FiremindQueueWorker {

    public static boolean shutDownOnEmptyQueue = false;

    public static void main(final String[] args) {
        parseArguments(args);

        int lastExitStatus = 0;
        while (!(shutDownOnEmptyQueue && lastExitStatus == 1)) {
            ProcessBuilder pb = new ProcessBuilder("java", "-noverify", "-cp",
                    "Magarena.jar", "magic.firemind.FiremindDuelRunner");
            pb.redirectErrorStream(true);
            try {
                Process p = pb.start();
                Reader reader = new InputStreamReader(p.getInputStream(), UTF_8);
                BufferedReader br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);

                p.waitFor();
                reader.close();
                lastExitStatus = p.exitValue();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (lastExitStatus != 0) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("Woken");
                }
            }
        }

    }

    private static boolean parseArguments(final String[] args) {
        boolean validArgs = true;
        for (final String curr : args) {
            if ("--self-terminate".equals(curr)) {
                shutDownOnEmptyQueue = true;
            }
        }
        return validArgs;
    }

}
