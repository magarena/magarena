package magic.ui;

import magic.model.MagicCardDefinition;

public interface ICardSelectionListener {
    void newCardSelected(final MagicCardDefinition card);
}
