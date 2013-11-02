package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.UndoClickedException;
import magic.ui.choice.MayChoicePanel;

import java.util.concurrent.Callable;

public class MagicScryChoice extends MagicMayChoice {
    public MagicScryChoice() {
        super("Put this card on the bottom of your library?");
    }

    @Override
    public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) throws UndoClickedException {
        
        final Object[] choiceResults=new Object[1];
        choiceResults[0]=NO_CHOICE;
        
        final MagicCard topCard = player.getLibrary().getCardAtTop();
        if (topCard == MagicCard.NONE) {
            return choiceResults;
        }
        
        final MagicCardList cards = new MagicCardList();
        cards.add(topCard);
        controller.showCards(cards);

        controller.disableActionButton(false);
        final MayChoicePanel choicePanel = controller.waitForInput(new Callable<MayChoicePanel>() {
            public MayChoicePanel call() {
                return new MayChoicePanel(controller,source,getDescription());
            }
        });
            
        controller.showCards(new MagicCardList());

        if (choicePanel.isYesClicked()) {
            choiceResults[0]=YES_CHOICE;
        }

        return choiceResults;
    }
}
