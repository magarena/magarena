package magic.model.target;

import magic.model.MagicCopyable;
import magic.model.MagicCardDefinition;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public interface MagicTarget extends MagicCopyable, MagicMappable {
    boolean     isPlayer();
    boolean     isSpell();
    boolean     isPermanent();
    boolean     isValidTarget(final MagicSource source);
    int         getPreventDamage();
    String      getName();
    MagicPlayer getController();
    void        setPreventDamage(int amount);
    MagicCardDefinition getCardDefinition();
}
