package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

public class Zealot_il_Vec {
    public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                        player + " may have " + permanent +
                        " deal 1 damage to target creature.",
                        MagicTargetChoice.NEG_TARGET_CREATURE),
                    new MagicDamageTargetPicker(3),
                    this,
                    player + " may$ have " + permanent +
                    " deal 1 damage to target creature$."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        final MagicPermanent permanent = event.getPermanent();
                        final MagicDamage damage = new MagicDamage(
                                permanent,
                                creature,
                                1,
                                false);
                        game.doAction(new MagicDealDamageAction(damage));
                        game.doAction(new MagicChangeStateAction(
                                permanent,
                                MagicPermanentState.NoCombatDamage,true));
                    }
                });
            }
        }
    };
}
