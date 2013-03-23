package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

import java.util.Collection;

public class Oros__the_Avenger {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();        
            return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            new MagicPayManaCostChoice(MagicManaCost.create("{2}{W}"))
                        ),
                        this,
                        "You may$ pay {2}{W}$. If you do, SN " +
                        "deals 3 damage to each nonwhite creature."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            if (event.isYes()) {
                final Collection<MagicPermanent> targets=
                    game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_NONWHITE_CREATURE);
                for (final MagicPermanent target : targets) {
                    final MagicDamage damage=new MagicDamage(event.getPermanent(),target,3);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }
        }
    };
}
