package magic.model;

import magic.model.event.MagicSourceActivation;

import java.util.Collection;

public interface MagicSource extends MagicCopyable, MagicObject {
    MagicGame getGame();
    Collection<MagicSourceActivation<? extends MagicSource>> getSourceActivations();

    public static final MagicSource NONE = MagicCard.NONE;
}
