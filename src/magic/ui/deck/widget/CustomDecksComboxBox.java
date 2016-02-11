package magic.ui.deck.widget;


import magic.utility.DeckUtils;
import java.nio.file.Paths;

@SuppressWarnings("serial")
public class CustomDecksComboxBox extends DeckFilesComboBox {

    public CustomDecksComboxBox() {
        super(Paths.get(DeckUtils.getDeckFolder()));
    }

}
