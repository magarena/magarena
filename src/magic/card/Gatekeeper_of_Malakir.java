package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Gatekeeper_of_Malakir {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            return (permanent.isKicked()) ?
                new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    this,
                    "Target player$ sacrifices a creature."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        MagicTargetChoice.SACRIFICE_CREATURE));
                }
            });
        }
    };
}
