package magic.ui.deck.widget;

import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class FiremindDecksComboxBox extends DeckFilesComboBox {

    public FiremindDecksComboxBox() {
        super(DeckUtils.getFiremindDecksFolder());
    }

}
