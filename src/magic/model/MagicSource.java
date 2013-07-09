package magic.model;

import magic.model.event.MagicActivation;
import magic.model.event.MagicSourceActivation;
import magic.model.event.MagicEvent;

import java.util.Collection;
import java.util.Set;

public interface MagicSource extends MagicCopyable, MagicObject {
    MagicGame getGame();
    Collection<MagicSourceActivation<? extends MagicSource>> getSourceActivations();
}
