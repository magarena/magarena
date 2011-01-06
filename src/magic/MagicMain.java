package magic;

import java.io.File;

import magic.data.CardDefinitions;
import magic.data.CardEventDefinitions;
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
		
	public static void main(String args[]) {		

		initializeCards();
		new MagicFrame();
	}
	
	public static String getGamePath() {

		return System.getProperty("user.home")+File.separatorChar+GAME_PATH;
	}
	
	public static String getModsPath() {
		
		return getGamePath()+File.separatorChar+MODS_PATH;
	}
	
	public static void initializeCards() {
		
		try {
			new File(getGamePath()).mkdir();
			DeckUtils.createDeckFolder();
			final CardDefinitions cardDefinitions=CardDefinitions.getInstance();
			cardDefinitions.loadCardDefinitions();
			KeywordDefinitions.getInstance().loadKeywordDefinitions();
			TriggerDefinitions.addTriggers();
			LocalVariableDefinitions.addLocalVariables();
			ManaActivationDefinitions.addManaActivations();
			PermanentActivationDefinitions.addPermanentActivations();
			CardEventDefinitions.setCardEvents();
			MagicStaticLocalVariable.initializeCardDefinitions();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
}