package magic.ui.widget.cards.table;

import magic.model.MagicCardDefinition;

public interface ICardSelectionListener {
    void newCardSelected(final MagicCardDefinition card);
}
