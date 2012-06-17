package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Scrib_Nibblers {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            final MagicPlayer player = permanent.getController();
            return (player == played.getController() && played.isLand()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new MagicSimpleMayChoice(
                            player + " may untap " + permanent + ".",
                            MagicSimpleMayChoice.UNTAP,
                            1,
                            MagicSimpleMayChoice.DEFAULT_YES),
                    new Object[]{permanent},
                    this,
                    player + " may$ untap " + permanent + "."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
            }
        }        
    };
    
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.MustAttack),
            "Attacks") {
        
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
            return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new Object[]{player},
                    this,
                    "Exile the top card of target player's$ library. " +
                    "If it's a land card, " + player + " gains 1 life.");
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final MagicCard card = player.getLibrary().getCardAtTop();
                    if (card != MagicCard.NONE) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
                        if (card.getCardDefinition().isLand()) {
                            game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
                        }
                    }
                }
            });
        }
    };
}
