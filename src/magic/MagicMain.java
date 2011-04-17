package magic;

import java.io.File;
import java.io.IOException;

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
		System.out.println("Data folder : "+gamePath);
	}
	
	public static void main(String args[]) {		

		initialize();
		if (new File(gamePath,"ubeefx.txt").exists()) {
			new MagicFrame();
		} else {
			System.out.println("Incompatible data folder!");
		}
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
			new File(getGamePath()).mkdir();
			new File(getModsPath()).mkdir();
			DeckUtils.createDeckFolder();
			initializeEngine();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
}