package magic.ui.widget.cards.table;

import java.util.List;
import magic.model.MagicCardDefinition;

@SuppressWarnings("serial")
public class DeckTableModel extends CardTableModel {

    public DeckTableModel(final List<MagicCardDefinition> cardDefs) {
        super(cardDefs);
    }

}

