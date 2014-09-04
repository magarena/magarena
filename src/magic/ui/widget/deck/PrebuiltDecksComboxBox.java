package magic.ui.widget.deck;

import magic.data.DeckUtils;

@SuppressWarnings("serial")
public class PrebuiltDecksComboxBox extends DeckFilesComboBox {

    public PrebuiltDecksComboxBox() {
        super(DeckUtils.getPrebuiltDecksFolder());
    }

}
