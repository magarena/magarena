package magic.card;

import java.util.EnumSet;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicAction;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPutIntoPlayAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Seance {
    private static final MagicStatic Spirit = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            flags.add(MagicSubType.Spirit);
        }
    };
   
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return new MagicEvent(
                permanent,
                player,
                new MagicMayChoice(
                        player + " may exile target creature card from your graveyard.",
                        MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD),
                        new MagicGraveyardTargetPicker(true),
                this,
                player + " may$ exile target creature card$ from his or her graveyard. " +
                "If he or she does, put a token onto the battlefield that's a copy " +
                "of that card except it's a Spirit in addition to its other types. " +
                "Exile it at the beginning of the next end step.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetCard(game,choiceResults,1,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        final MagicPlayer player=event.getPlayer();
                        game.doAction(new MagicRemoveCardAction(
                                card,
                                MagicLocationType.Graveyard));
                        game.doAction(new MagicMoveCardAction(
                                card,
                                MagicLocationType.Graveyard,
                                MagicLocationType.Exile));
                        final MagicAction action = new MagicPlayTokenAction(
                                player,
                                card.getCardDefinition());
                        game.doAction(action);
                        final MagicPermanent permanent = ((MagicPutIntoPlayAction)action).getPermanent();
                        game.doAction(new MagicChangeStateAction(
                                permanent,
                                MagicPermanentState.SacrificeAtEndOfTurn,
                                true));
                        game.doAction(new MagicAddStaticAction(permanent,Spirit));
                    }
                });
            }
        }
    };
}
