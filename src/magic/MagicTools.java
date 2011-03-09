package magic;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

public class MagicTools {

	private static void listAllCards() throws IOException {
		
		CardDefinitions.getInstance().loadCardDefinitions();
		
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
	
	public static void main(final String args[]) throws Exception {

		listAllCards();
	}
}