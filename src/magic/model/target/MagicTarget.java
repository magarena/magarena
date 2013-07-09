package magic.model.target;

import magic.model.MagicCardDefinition;
import magic.model.MagicCopyable;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicObject;
import magic.model.MagicGame;

public interface MagicTarget extends MagicCopyable, MagicObject {
    boolean isLegalTarget(final MagicPlayer player, final MagicTargetFilter<? extends MagicTarget> targetFilter);
    boolean isValidTarget(final MagicSource source);
    int     getPreventDamage();
    void    setPreventDamage(int amount);
}
