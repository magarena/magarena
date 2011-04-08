package magic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
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
	
	static void moveCardImages() throws Exception {

		final Map<String,String> cardUrls=new HashMap<String,String>();
		final BufferedReader reader=new BufferedReader(new FileReader("resources/magic/data/images.txt"));
		while (true) {
			
			String line=reader.readLine();
			if (line == null) {
				break;
			}
			line=line.trim();
			if (!line.isEmpty()&&!line.startsWith(">")) {
				final int index=line.indexOf(";");
				final String name=line.substring(0,index-4);
				final String url=line.substring(index+1);
				System.out.println(name+"="+url);
				cardUrls.put(name,url);				
			}
		}
		reader.close();
		
		final BufferedReader reader2=new BufferedReader(new FileReader("resources/magic/data/cards.txt"));
		final BufferedWriter writer2=new BufferedWriter(new FileWriter("/temp/cards.txt"));
		while (true) {

			String line=reader2.readLine();
			if (line == null) {
				break;
			}
			line=line.trim();
			writer2.write(line);
			writer2.newLine();
			if (line.startsWith(">")) {
				final String cardName=line.substring(1);
				writer2.write("image="+cardUrls.get(cardName));
				writer2.newLine();
				final boolean defaultCube=CubeDefinitions.getInstance().getCubeDefinition("ubeefx").contains(cardName);
				final boolean allCube=CubeDefinitions.getInstance().getCubeDefinition("singularita").contains(cardName);
				if (defaultCube) {
					writer2.write("cube=default");
					writer2.newLine();
				} else if (allCube) {
					writer2.write("cube=all");
					writer2.newLine();
				}
			}
		}
		reader2.close();
		writer2.close();
	}
	
	public static void main(final String args[]) throws Exception {

		MagicMain.initializeEngine();
		moveCardImages();
	}
}