package magic.model;

import magic.model.MagicCardDefinition;
import magic.model.MagicCopyable;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public abstract class MagicObjectImpl implements MagicObject {
    @Override
    public boolean isFriend(final MagicObject other) {
        return getController() == other.getController();
    }

    @Override
    public boolean isEnemy(final MagicObject other) {
        return getOpponent() == other.getController();
    }
    
    @Override
    public MagicPlayer getOpponent() {
        return getController().getOpponent();
    }
    
    @Override
    public boolean isCreature() {
        return isPermanent() && hasType(MagicType.Creature);
    }
    
    @Override
    public boolean isPlaneswalker() {
        return isPermanent() && hasType(MagicType.Planeswalker);
    }
}
