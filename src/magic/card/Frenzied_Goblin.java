package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Frenzied_Goblin {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player=permanent.getController();
            return (permanent==creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            "You may pay {R}.",
                            new MagicPayManaCostChoice(MagicManaCost.RED),
                            MagicTargetChoice.NEG_TARGET_CREATURE),
                        new MagicNoCombatTargetPicker(false,true,false),
                        MagicEvent.NO_DATA,
                        this,
                        "You may$ pay {R}$. If you do, target creature$ can't block this turn."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,2,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotBlock));
                    }
                });
            }
        }
    };
}
