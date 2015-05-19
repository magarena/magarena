package magic.model;

import magic.model.event.MagicEvent;

public abstract class MagicAmount {

    public abstract int getAmount(final MagicSource source, final MagicPlayer player);
    
    public int getAmount(final MagicEvent event) {
        return getAmount(event.getSource(), event.getPlayer());
    }
}
