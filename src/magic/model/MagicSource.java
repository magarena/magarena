package magic.model;

import java.util.Collection;
import magic.model.event.MagicSourceActivation;

public interface MagicSource extends MagicCopyable, MagicObject {
    MagicGame getGame();
    Collection<MagicSourceActivation<? extends MagicSource>> getSourceActivations();
    
    MagicSource NONE = MagicCard.NONE;
}
