package magic.ui.widget.deck;

import magic.data.DeckUtils;

@SuppressWarnings("serial")
public class FiremindDecksComboxBox extends DeckFilesComboBox {

    public FiremindDecksComboxBox() {
        super(DeckUtils.getFiremindDecksFolder());
    }

}
