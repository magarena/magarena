package magic;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.data.DeckUtils;
import magic.data.KeywordDefinitions;
import magic.model.variable.MagicStaticLocalVariable;
import magic.ui.MagicFrame;

import javax.swing.*;
import java.io.File;

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
	
	public static void main(String args[]) {		
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

	public static void initializeEngine() {
        CardDefinitions.getInstance().loadCardDefinitions();
        CubeDefinitions.getInstance().loadCubeDefinitions();
        KeywordDefinitions.getInstance().loadKeywordDefinitions();
        MagicStaticLocalVariable.initializeCardDefinitions();
	}
	
	public static void initialize() {
        boolean madeGamePath = new File(getGamePath()).mkdir();
        if (!madeGamePath) {
            System.err.println("Unable to create directory " + getGamePath());
        }

        boolean madeModsPath = new File(getModsPath()).mkdir();
        if (!madeModsPath) {
            System.err.println("Unable to create directory " + getModsPath());
        }
        
        DeckUtils.createDeckFolder();
        initializeEngine();
	}
}
