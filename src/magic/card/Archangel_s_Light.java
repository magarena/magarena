package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Archangel_s_Light {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack,player},
                    this,
                    player + " gains 2 life for each card in his or her " +
                    "graveyard, then shuffles his or her graveyard into " +
                    "his or her library.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = (MagicPlayer)data[1];
            final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
            for (final MagicCard card : graveyard) {
                game.doAction(new MagicChangeLifeAction(player,2));
                game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.OwnersLibrary));            
            }
            game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
        }
    };
}
