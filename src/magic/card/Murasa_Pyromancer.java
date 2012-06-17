package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Murasa_Pyromancer {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            return (otherPermanent.getController() == player &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                                player + " may have " + permanent + " deal " +
                                "damage to target creature equal to the " +
                                "number of Allies he or she controls.",
                                MagicTargetChoice.NEG_TARGET_CREATURE),
                        // estimated. Amount of damage can be different on resolution
                        new MagicDamageTargetPicker(player.getNrOfPermanentsWithSubType(MagicSubType.Ally)),
                        new Object[]{player,permanent},
                        this,
                        player + " may$ have " + permanent + " deal " +
                        "damage to target creature$ equal to the " +
                        "number of Allies he or she controls.") :
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
                        final MagicPlayer player = (MagicPlayer)data[0];
                        final int amount =
                                player.getNrOfPermanentsWithSubType(MagicSubType.Ally);
                        if (amount > 0) {
                            final MagicDamage damage = new MagicDamage(
                                    (MagicPermanent)data[1],
                                    creature,
                                    amount,
                                    false);
                            game.doAction(new MagicDealDamageAction(damage));
                        }
                    }
                });
            }            
        }        
    };
}
