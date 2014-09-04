package magic.ui.widget.deck;

import magic.data.DeckUtils;

import java.nio.file.Paths;

@SuppressWarnings("serial")
public class CustomDecksComboxBox extends DeckFilesComboBox {

    public CustomDecksComboxBox() {
        super(Paths.get(DeckUtils.getDeckFolder()));
    }

}
