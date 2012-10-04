package magic.model.target;

import magic.model.MagicCardDefinition;
import magic.model.MagicCopyable;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicObject;

public interface MagicTarget extends MagicCopyable, MagicMappable, MagicObject {
    boolean isValidTarget(final MagicSource source);
    int     getPreventDamage();
    void    setPreventDamage(int amount);
}
