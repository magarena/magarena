package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

import java.util.Collection;

public class Ronin_Cliffrider {
    public static final MagicWhenAttacksTrigger T3 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may$ have SN deal 1 damage " +
                    "to each creature defending player controls.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final MagicSource source = event.getSource();
                final MagicPlayer defendingPlayer = event.getPlayer().getOpponent();
                final Collection<MagicTarget> creatures =
                        game.filterTargets(defendingPlayer,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                for (final MagicTarget creature : creatures) {
                    final MagicDamage damage = new MagicDamage(source,creature,1,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }
        }
    };
}
