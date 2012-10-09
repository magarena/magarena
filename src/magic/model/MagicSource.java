package magic.model;

import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;

import java.util.Collection;

public interface MagicSource extends MagicCopyable, MagicMappable, MagicObject {
    boolean   hasAbility(final MagicAbility ability);    
    boolean   hasColor(final MagicColor color);
    MagicGame getGame();
    Collection<MagicActivation> getActivations();
}
