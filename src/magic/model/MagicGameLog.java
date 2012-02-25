package magic.model;

import magic.MagicMain;
import magic.ui.VersionPanel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MagicGameLog {
    
	private static String gameLog = MagicMain.getGamePath() + File.separator + "game.log";
	private static PrintWriter writer = null;
	
    private MagicGameLog() {}
    
    public static void initialize() {
    	try {
			writer = new PrintWriter(gameLog);
			final StringBuilder sb = new StringBuilder();
	        sb.append("MAGARENA GAME LOG");
	        sb.append('\n');
	        sb.append("CREATED ON " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()));
	        sb.append('\n');
	        sb.append("MAGARENA VERSION " + VersionPanel.getVersion());
	        sb.append(", JRE " + System.getProperty("java.version"));
	        sb.append(", OS " + System.getProperty("os.name"));
	        sb.append("_" + System.getProperty("os.version"));
	        sb.append(" " + System.getProperty("os.arch"));
	        sb.append("\n\n");
	        log(sb.toString());
		} catch (FileNotFoundException e) {
			System.err.println("Unable to create game log");
		}
    }
    
    public static void log(final String message) {
    	if (writer != null) {
    		writer.println(message);
        	writer.flush();
    	}	
    }
    
    public static void close() {
    	if (writer != null) {
    		writer.close();
    	}
    }
}
