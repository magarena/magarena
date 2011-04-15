package magic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
		final String filenames[] = new File(MagicMain.getGamePath(),"cards").list();
		for (final String filename : filenames) {
			
			final String name = filename.substring(0,filename.length()-4);
			if (cardDefinitions.getCard(name) == null) {
				System.out.println(name);
			}
		}
	}
	
	static void removeCardImages() throws Exception {

		final BufferedReader reader2=new BufferedReader(new FileReader("resources/magic/data/cards2.txt"));
		final BufferedWriter writer2=new BufferedWriter(new FileWriter("/temp/cards2.txt"));
		while (true) {

			String line=reader2.readLine();
			if (line == null) {
				break;
			}
			line=line.trim();
			if (!line.startsWith("image")) {
				writer2.write(line);
				writer2.newLine();
			}
		}
		reader2.close();
		writer2.close();
	}
	
	public static void main(final String args[]) throws Exception {

		MagicMain.initializeEngine();
		removeCardImages();
	}
}