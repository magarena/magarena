package magic.ui.widget.deck;


import magic.utility.DeckUtils;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public abstract class DeckFilesComboBox extends JComboBox<String> {

    private final static String ALL_DECKS = "*" + DeckUtils.DECK_EXTENSION;

    public DeckFilesComboBox(final Path decksDirectory) {
        this.setModel(new DefaultComboBoxModel<String>(getDeckNamesArray(decksDirectory)));
    }

    private String[] getDeckNamesArray(final Path decksDirectory) {
        final List<String> deckNamesList = new ArrayList<String>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(decksDirectory, ALL_DECKS)) {
          for (Path p : ds) {
              deckNamesList.add(DeckUtils.getDeckNameFromFile(p));
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
      return deckNamesList.toArray(new String[deckNamesList.size()]);
    }

}
