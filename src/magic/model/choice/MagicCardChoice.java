package magic.model.choice;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.exception.UndoClickedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import magic.model.IUIGameController;

public class MagicCardChoice extends MagicChoice {

    private static final String MESSAGE="Choose a card from your hand.";

    private final int amount;

    public MagicCardChoice(final int amount) {

        super(genDescription(amount));
        this.amount=amount;
    }

    private static final String genDescription(final int amount) {
        if (amount==1) {
            return "Choose a card from your hand.";
        } else {
            return "Choose " + amount + " cards from your hand.";
        }
    }

    private void createOptions(
        final Collection<Object> options,
        final MagicCardList hand,
        final MagicCard[] cards,
        final int count,
        final int aAmount,
        final int index
    ) {

        if (count == aAmount) {
            options.add(new MagicCardChoiceResult(cards));
            return;
        }

        final int left = hand.size() - index;
        if (count + left < aAmount) {
            return;
        }

        cards[count]=hand.get(index);
        createOptions(options,hand,cards,count+1,aAmount,index+1);
        createOptions(options,hand,cards,count,aAmount,index+1);
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final List<Object> options = new ArrayList<>();
        final MagicCardList hand = new MagicCardList(player.getHand());
        hand.remove(source);
        Collections.sort(hand);
        final int actualAmount = Math.min(amount,hand.size());
        if (actualAmount > 0) {
            createOptions(options,hand,new MagicCard[actualAmount],0,actualAmount,0);
        } else {
            options.add(new MagicCardChoiceResult());
        }
        return options;
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final MagicCardChoiceResult result=new MagicCardChoiceResult();
        final Set<Object> validCards=new HashSet<Object>(player.getHand());
        validCards.remove(source);
        int actualAmount=Math.min(amount,validCards.size());
        for (;actualAmount>0;actualAmount--) {
            final String message=result.size()>0?result.toString()+"|"+MESSAGE:MESSAGE;
            controller.focusViewers(0);
            controller.disableActionButton(false);
            controller.setValidChoices(validCards,false);
            controller.showMessage(source,message);
            controller.waitForInput();
            final MagicCard card = controller.getChoiceClicked();
            validCards.remove(card);
            result.add(card);
        }
        return new Object[]{result};
    }

}
