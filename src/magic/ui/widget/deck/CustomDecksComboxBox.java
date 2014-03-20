package magic.ui.widget.deck;

import java.nio.file.Paths;

import magic.data.DeckUtils;

@SuppressWarnings("serial")
public class CustomDecksComboxBox extends DeckFilesComboBox {

    public CustomDecksComboxBox() {
        super(Paths.get(DeckUtils.getDeckFolder()));
    }

}
