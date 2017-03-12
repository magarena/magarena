package magic.ui.widget.deck;

import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class CustomDecksComboxBox extends DeckFilesComboBox {

    public CustomDecksComboxBox() {
        super(DeckUtils.getDecksFolder());
    }

}
