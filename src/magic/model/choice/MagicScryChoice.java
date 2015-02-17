package magic.model.choice;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.exception.UndoClickedException;
import magic.ui.duel.choice.MayChoicePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import magic.model.IGameController;

public class MagicScryChoice extends MagicMayChoice {
    public MagicScryChoice() {
        super("Move this card from the top of your library to the bottom?");
    }
    
    @Override
    public List<Object[]> getArtificialChoiceResults(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        
        if (player.getLibrary().isEmpty()) {
            final List<Object[]> choiceResultsList=new ArrayList<Object[]>();
            choiceResultsList.add(new Object[]{NO_CHOICE});
            return choiceResultsList;
        } else {
            return NO_OTHER_CHOICE_RESULTS;
        }
    }

    @Override
    public Object[] getPlayerChoiceResults(
            final IGameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) throws UndoClickedException {
        
        final Object[] choiceResults=new Object[1];
        choiceResults[0]=NO_CHOICE;
        
        if (player.getLibrary().isEmpty()) {
            return choiceResults;
        }
        
        final MagicCardList cards = new MagicCardList();
        cards.add(player.getLibrary().getCardAtTop());
        controller.showCards(cards);

        controller.disableActionButton(false);
        final MayChoicePanel choicePanel = controller.waitForInput(new Callable<MayChoicePanel>() {
            public MayChoicePanel call() {
                return new MayChoicePanel(controller,source,getDescription());
            }
        });
            
        controller.clearCards();

        if (choicePanel.isYesClicked()) {
            choiceResults[0]=YES_CHOICE;
        }

        return choiceResults;
    }
}
