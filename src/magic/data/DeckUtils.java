package magic.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import magic.MagicMain;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicRandom;

public class DeckUtils {

	public static final String DECK_EXTENSION=".dec";
	
	public static final FileFilter DECK_FILEFILTER=new FileFilter() {

		@Override
		public boolean accept(final File file) {
			
			return file.isDirectory()||file.getName().endsWith(DECK_EXTENSION);
		}

		@Override
		public String getDescription() {

			return "Magarena deck";
		}
	};
	
	private static final String CARD_TYPES[]={"creatures","spells","lands"};
	
	public static String getDeckFolder() {
		
		return MagicMain.getGamePath()+File.separator+"decks";
	}
	
	public static void createDeckFolder() {
		final File deckFolderFile=new File(getDeckFolder());
		if (!deckFolderFile.exists()) {
			final boolean isCreated = deckFolderFile.mkdir();
            if (!isCreated) {
                System.err.println("WARNING. Unable to create " + getDeckFolder());
            }
		}
	}
	
	public static void saveDeck(final String filename,final MagicPlayerDefinition player) {

		final List<SortedMap<String,Integer>> cardMaps=new ArrayList<SortedMap<String,Integer>>();

        for (int count=3;count>0;count--) {
			cardMaps.add(new TreeMap<String, Integer>());
		}
		
		for (final MagicCardDefinition cardDefinition : player.getDeck()) {
			final String name=cardDefinition.getName();
			int index;
			if (cardDefinition.isLand()) {
				index=2;
			} else if (cardDefinition.isCreature()) {
				index=0;
			} else {
				index=1;
			}
			final SortedMap<String,Integer> cardMap=cardMaps.get(index);
			Integer count=cardMap.get(name);
			cardMap.put(name,count==null?Integer.valueOf(1):Integer.valueOf(count+1));
		}
		
		try {						
			final BufferedWriter writer=new BufferedWriter(new FileWriter(filename));
			for (int index=0;index<=2;index++) {
				final SortedMap<String,Integer> cardMap=cardMaps.get(index);
				if (!cardMap.isEmpty()) {
					writer.write("# "+cardMap.size()+" "+CARD_TYPES[index]);
					writer.newLine();
					for (final Map.Entry<String,Integer> entry : cardMap.entrySet()) {
						writer.write(entry.getValue()+" "+entry.getKey());
						writer.newLine();
					}
					writer.newLine();
				}
			}			
			writer.close();
		} catch (final IOException ex) {
            System.err.println("ERROR! Unable to save deck");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
	}
	
	public static void loadDeck(final String filename,final MagicPlayerDefinition player) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (final FileNotFoundException ex) {
            reader = null;
        }
        final int colorCount[] = new int[MagicColor.NR_COLORS];
        final MagicDeck deck = new MagicDeck();
        deck.setName(new File(filename).getName());

        while (true) {
            String line = null;
            try {
                line = reader.readLine();
            } catch (final IOException ex) {
                line = null;
            }
            if (line == null) {
                break;
            }
            line = line.trim();
            if (!line.isEmpty()&&!line.startsWith("#")) {
                int index = line.indexOf(' ');
                int amount = Integer.parseInt(line.substring(0,index));
                final String name=line.substring(index+1).trim();
                final MagicCardDefinition cardDefinition = 
                    CardDefinitions.getInstance().getCard(name);
                for (int count=amount;count>0;count--) {
                    final int colorFlags=cardDefinition.getColorFlags();
                    for (final MagicColor color : MagicColor.values()) {
                        if (color.hasColor(colorFlags)) {
                            colorCount[color.ordinal()]++;
                        }
                    }
                    deck.add(cardDefinition);
                }
            }
        }
        
        // Find up to 3 of the most common colors in the deck.
        final StringBuffer colorText=new StringBuffer();
        while (colorText.length()<3) {
            int maximum=0;
            int index=0;
            for (int i=0;i<colorCount.length;i++) {
                if (colorCount[i]>maximum) {
                    maximum=colorCount[i];
                    index=i;
                }
            }
            if (maximum==0) {
                break;
            }
            colorText.append(MagicColor.values()[index].getSymbol());
            colorCount[index]=0;
        }
        player.setProfile(new MagicPlayerProfile(colorText.toString()));
        player.setDeck(deck);			
        
        try {
            if (reader != null) reader.close();
        } catch (final IOException ex) {
            //do nothing
        }
	}
	
	private static void retrieveDeckFiles(final File folder,final List<File> deckFiles) {
		
		final File files[]=folder.listFiles();
		for (final File file : files) {
			
			if (file.isDirectory()) {
				retrieveDeckFiles(file,deckFiles);
			} else if (file.getName().endsWith(DECK_EXTENSION)) {
				deckFiles.add(file);
			}
		}
	}
	
	public static void loadRandomDeck(final MagicPlayerDefinition player) {
		
		final File deckFile=new File(getDeckFolder());
		final List<File> deckFiles=new ArrayList<File>();
		retrieveDeckFiles(deckFile,deckFiles);		
		final int size=deckFiles.size();
		if (size==0) {
			// Creates a simple default deck.
			final MagicDeck deck=new MagicDeck();
			deck.setName("Default.dec");
			final MagicCardDefinition creature=CardDefinitions.getInstance().getCard("Elite Vanguard");
			final MagicCardDefinition land=CardDefinitions.getInstance().getCard("Plains");
			for (int count=24;count>0;count--) {
				
				deck.add(creature);
			}
			for (int count=16;count>0;count--) {
				
				deck.add(land);
			}
			player.setProfile(new MagicPlayerProfile("w"));
			player.setDeck(deck);
		} else {
			loadDeck(deckFiles.get(MagicRandom.nextInt(size)).toString(),player);
		}
	}
}
