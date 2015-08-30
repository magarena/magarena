package magic.ui.widget.deck;

import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class FiremindDecksComboxBox extends DeckFilesComboBox {

    public FiremindDecksComboxBox() {
        super(DeckUtils.getFiremindDecksFolder());
    }

}
