package magic;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

public class MagicTools {

	static void listAllCards() {
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
	
	static void checkCards() {
		final CardDefinitions cardDefinitions = CardDefinitions.getInstance();		
		final String filenames[] = new File(MagicMain.getGamePath(),"cards").list();
		final Set<MagicCardDefinition> remaining = new HashSet<MagicCardDefinition>(CardDefinitions.getInstance().getCards());
		for (final String filename : filenames) {
			final String name = filename.substring(0,filename.length()-4);
			final MagicCardDefinition cardDefinition = cardDefinitions.getCard(name);
			if (cardDefinition == null) {
				System.out.println(">"+name);
			} else {
				remaining.remove(cardDefinition);
			}
		}
		for (final MagicCardDefinition card : remaining) {
			if (!card.isToken()) {
				System.out.println("<"+card.getName());
			}
		}
	}
		
	public static void main(final String args[]) {
		MagicMain.initializeEngine();
		checkCards();
	}
}
