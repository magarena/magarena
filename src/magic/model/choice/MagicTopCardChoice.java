package magic.model.choice;

import java.util.List;

import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

/**
 * Choice about top card of player's library. Used for 'scry 1' and 'surveil 1'.
 */
public class MagicTopCardChoice extends MagicMayChoice {
    public MagicTopCardChoice(final String description) {
        super(description);
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        if (player.getLibrary().isEmpty()) {
            return NO_CHOICE_LIST;
        } else {
            return NO_OTHER_CHOICE_RESULTS;
        }
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game,
                                           final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final Object[] choiceResults = new Object[1];
        choiceResults[0] = NO_CHOICE;

        if (player.getLibrary().isEmpty()) {
            return choiceResults;
        }

        final MagicCardList cards = new MagicCardList();
        cards.add(player.getLibrary().getCardAtTop());
        controller.showCardsToChoose(cards);

        controller.disableActionButton(false);

        if (controller.getMayChoice(source, getDescription())) {
            choiceResults[0] = YES_CHOICE;
        }

        controller.clearCardsToChoose();

        return choiceResults;
    }
}
