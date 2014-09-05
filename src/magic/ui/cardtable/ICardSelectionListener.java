package magic.ui.cardtable;

import magic.model.MagicCardDefinition;

public interface ICardSelectionListener {
    void newCardSelected(final MagicCardDefinition card);
}
