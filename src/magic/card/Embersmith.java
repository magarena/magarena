package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;

public class Embersmith {
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
            final MagicPlayer player = permanent.getController();
            final MagicCard card = data.getCard();
            return (card.getOwner() == player &&
                    data.getCardDefinition().isArtifact()) ?
                        new MagicEvent(
                            permanent,
                            player,
                                new MagicMayChoice(
                                    "You may pay {1}.",
                                    new MagicPayManaCostChoice(MagicManaCost.ONE),
                                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER),
                                    new MagicDamageTargetPicker(2),
                                new Object[]{permanent},
                                this,
                                "You may$ pay {1}$. If you do, " + permanent + " deals 1 " +
                                "damage to target creature or player$."):
                       MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTarget(game,choiceResults,2,new MagicTargetAction() {
                    public void doAction(final MagicTarget target) {
                        final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],target,1,false);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                });
            }
        }        
    };
}
