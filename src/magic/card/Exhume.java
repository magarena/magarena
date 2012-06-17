package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Exhume {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(true),
                    new Object[]{cardOnStack,player},
                    this,
                    "Each player puts a creature card from " +
                    "his or her graveyard onto the battlefield.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            game.doAction(new MagicMoveCardAction(cardOnStack));
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    final MagicPlayer player = (MagicPlayer)data[1];
                    game.doAction(new MagicReanimateAction(player,targetCard,MagicPlayCardAction.NONE));
                    game.addEvent(new MagicEvent(
                            cardOnStack.getCard(),
                            game.getOpponent(player),
                            MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                            new MagicGraveyardTargetPicker(true),
                            new Object[]{game.getOpponent(player)},
                            EVENT_ACTION,
                            ""));
                }
            });
        }
        private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] data,
                    final Object[] choiceResults) {
                event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        final MagicPlayer opponent = (MagicPlayer)data[0];
                        game.doAction(new MagicReanimateAction(opponent,card,MagicPlayCardAction.NONE));
                    }
                });
            }
        };
    };
}
