package magic.model;

import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;

import java.util.Collection;

public interface MagicSource extends MagicCopyable, MagicMappable, MagicObject {
    boolean   hasAbility(final MagicAbility ability);    
    int       getColorFlags();
    MagicGame getGame();
    Collection<MagicActivation> getActivations();
}
