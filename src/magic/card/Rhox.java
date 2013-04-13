package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Rhox {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent attacker) {
            return (permanent == attacker) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may$ have SN deal its combat damage " +
                    "to defending player as though it weren't blocked."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                final MagicPermanent permanent = event.getPermanent();
                final MagicDamage damage = MagicDamage.Combat(
                    permanent,
                    event.getPlayer().getOpponent(),
                    permanent.getPower()
                );
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicChangeStateAction(
                    permanent,
                    MagicPermanentState.NoCombatDamage,
                    true
                ));
            }
        }
    };
}
