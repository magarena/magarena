package magic.model;

import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;

import java.util.Collection;

public interface MagicSource extends MagicCopyable, MagicMappable, MagicObject {
    MagicGame getGame();
    Collection<MagicActivation> getActivations();
}
