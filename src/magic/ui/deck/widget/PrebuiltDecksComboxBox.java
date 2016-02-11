package magic.ui.deck.widget;

import magic.utility.DeckUtils;

@SuppressWarnings("serial")
public class PrebuiltDecksComboxBox extends DeckFilesComboBox {

    public PrebuiltDecksComboxBox() {
        super(DeckUtils.getPrebuiltDecksFolder());
    }

}
