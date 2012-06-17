package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Forge_Devil {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_CREATURE,
                    new MagicDamageTargetPicker(1),
                    new Object[]{permanent,player},
                    this,
                    permanent + " deals 1 damage to target " +
                    "creature$ and 1 damage to " + player + ".");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = (MagicPermanent)data[0];
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(permanent,target,1,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
            final MagicDamage damage = new MagicDamage(permanent,(MagicPlayer)data[1],1,false);
            game.doAction(new MagicDealDamageAction(damage));
        }
    };
}
