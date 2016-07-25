package magic.model.choice;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.exception.UndoClickedException;
import java.util.ArrayList;
import java.util.List;
import magic.model.IUIGameController;

public class MagicScryChoice extends MagicMayChoice {
    public MagicScryChoice() {
        super("Move this card from the top of the library to the bottom?");
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();
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
