package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicPermanent;
import magic.model.MagicPayedCost;
import magic.model.MagicCardDefinition;

public abstract class MagicWhenSelfCombatDamagePlayerTrigger extends MagicWhenDamageIsDealtTrigger {
    public MagicWhenSelfCombatDamagePlayerTrigger() {}
    
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return super.accept(permanent, damage) && 
               damage.isSource(permanent) && damage.isCombat() && damage.isTargetPlayer();
    }
}
