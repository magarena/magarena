package magic;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.data.DeckUtils;
import magic.data.KeywordDefinitions;
import magic.ui.MagicFrame;
import magic.ui.widget.BlankPainter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class MagicMain {
	
	private static final String GAME_PATH="Magarena";
	private static final String MODS_PATH="mods";
	
    private static final String gamePath;
	
	static {
		final File dataDirFile=new File(System.getProperty("user.dir"),GAME_PATH);
		if (dataDirFile.exists()&&dataDirFile.isDirectory()) {
			gamePath=dataDirFile.toString();
		} else {		
			gamePath=System.getProperty("user.home")+File.separatorChar+GAME_PATH;		
		}
		System.err.println("Data folder : "+gamePath);
	}
	
	public static void main(final String args[]) {	
        Thread.setDefaultUncaughtExceptionHandler(new magic.model.MagicGameReport());
		
		// try to set the look and feel
	    try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
			
			// customize nimbus look
			UIManager.getLookAndFeelDefaults().put("Table.showGrid", true);
			UIManager.getLookAndFeelDefaults().put("ScrollPane[Enabled].borderPainter", new BlankPainter()); // removes hardcoded border
		} 
		catch (Exception e) {
			System.err.println("Unable to set look and feel. Probably missing the latest version of Java 6.");
			e.printStackTrace();
		}
		
		initialize();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MagicFrame();
            }
        });
	}	
		

	public static String getGamePath() {
		return gamePath;
	}
	
	public static String getModsPath() {
		return getGamePath()+File.separatorChar+MODS_PATH;
	}

	static void initializeEngine() {
        CardDefinitions.getInstance().loadCardDefinitions();
        CubeDefinitions.getInstance().loadCubeDefinitions();
        KeywordDefinitions.getInstance().loadKeywordDefinitions();
	}
	
	private static void initialize() {
        final boolean madeGamePath = new File(getGamePath()).mkdir();
        if (!madeGamePath) {
            System.err.println("Unable to create directory " + getGamePath());
        }

        final boolean madeModsPath = new File(getModsPath()).mkdir();
        if (!madeModsPath) {
            System.err.println("Unable to create directory " + getModsPath());
        }
        
        DeckUtils.createDeckFolder();
        initializeEngine();
	}
}
