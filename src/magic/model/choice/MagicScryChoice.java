package magic.model.choice;

import java.util.List;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

public class MagicScryChoice extends MagicMayChoice {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Move this card from the top of the library to the bottom?";

    public MagicScryChoice() {
        super(_S1);
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
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final Object[] choiceResults=new Object[1];
        choiceResults[0]=NO_CHOICE;

        if (player.getLibrary().isEmpty()) {
            return choiceResults;
        }

        final MagicCardList cards = new MagicCardList();
        cards.add(player.getLibrary().getCardAtTop());
        controller.showCards(cards);

        controller.disableActionButton(false);

        if (controller.getMayChoice(source, getDescription())) {
            choiceResults[0]=YES_CHOICE;
        }

        controller.clearCards();

        return choiceResults;
    }
}
