package magic;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

public class MagicTools {

	static void listAllCards() throws IOException {
				
		final SortedSet<String> names = new TreeSet<String>();
		for (final MagicCardDefinition cardDefinition : CardDefinitions.getInstance().getCards()) {
			
			if (!cardDefinition.isToken()) {
				names.add(cardDefinition.getName());
			}
		}
		for (final String name : names) {
			
			System.out.println(name);
		}
	}
	
	static void checkCards() throws IOException {
		
		final CardDefinitions cardDefinitions = CardDefinitions.getInstance();		
		final String filenames[] = new File(MagicMain.getGamePath(),"hqcards").list();
		for (final String filename : filenames) {
			
			final String name = filename.substring(0,filename.length()-4);
			if (cardDefinitions.getCard(name) == null) {
				System.out.println(name);
			}
		}
	}
	
	public static void main(final String args[]) throws Exception {

		MagicMain.initializeEngine();
		checkCards();
	}
}