package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicLifeChangeTriggerData;
import magic.model.trigger.MagicWhenLifeIsGainedTrigger;

public class Searing_Meditation {
    public static final MagicWhenLifeIsGainedTrigger T = new MagicWhenLifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            return permanent.isController(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}")),
                        MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER
                    ),
                    new MagicDamageTargetPicker(2),
                    this,
                    "You may$ pay {2}$. If you do, SN deals 2 " +
                    "damage to target creature or player$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTarget(game,choiceResults,2,new MagicTargetAction() {
                    public void doAction(final MagicTarget target) {
                        final MagicDamage damage = new MagicDamage(event.getPermanent(),target,2);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                });
            }
        }
    };
}
