package magic;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MagicTools {

    private static void listAllCards() {
        final SortedSet<String> names = new TreeSet<String>();
        for (final MagicCardDefinition cardDefinition : CardDefinitions.getCards()) {
            if (!cardDefinition.isToken()) {
                names.add(cardDefinition.getName());
            }
        }
        for (final String name : names) {
            System.out.println(name);
        }
    }
    
    private static void checkCards() {
        final String filenames[] = new File(MagicMain.getGamePath(),"cards").list();
        final Set<MagicCardDefinition> remaining = new HashSet<MagicCardDefinition>(CardDefinitions.getCards());
        for (final String filename : filenames) {
            final String name = filename.substring(0,filename.length()-4);
            final MagicCardDefinition cardDefinition = CardDefinitions.getCard(name);
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
