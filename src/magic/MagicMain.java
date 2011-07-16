package magic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.SwingUtilities;

import magic.data.CardDefinitions;
import magic.data.CardEventDefinitions;
import magic.data.CubeDefinitions;
import magic.data.DeckUtils;
import magic.data.KeywordDefinitions;
import magic.data.LocalVariableDefinitions;
import magic.data.ManaActivationDefinitions;
import magic.data.PermanentActivationDefinitions;
import magic.data.TriggerDefinitions;
import magic.model.variable.MagicStaticLocalVariable;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.MagicFrame;

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
	
	public static void main(String args[]) throws IOException {		
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

	public static void initializeEngine() throws IOException {
		CardDefinitions.getInstance().loadCardDefinitions();
		CubeDefinitions.getInstance().loadCubeDefinitions();
		KeywordDefinitions.getInstance().loadKeywordDefinitions();
		TriggerDefinitions.addTriggers();
		LocalVariableDefinitions.addLocalVariables();
		ManaActivationDefinitions.addManaActivations();
		PermanentActivationDefinitions.addPermanentActivations();
		CardEventDefinitions.setCardEvents();
		MagicStaticLocalVariable.initializeCardDefinitions();
	}
	
	public static void initialize() {
		try {
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
		} catch (final Exception ex) {
            System.err.println("ERROR! Unable to initialize the engine");
            System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
