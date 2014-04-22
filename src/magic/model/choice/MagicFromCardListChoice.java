package magic.model.choice;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.UndoClickedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MagicFromCardListChoice extends MagicChoice {

    private static final String MESSAGE="Choose a card.";
    private final MagicCardList showList;
    private final List<MagicCard> choiceList;
    private final int amount;
    private final boolean upTo;
    
    public MagicFromCardListChoice(final List<MagicCard> showList) {
        this(new MagicCardList(), showList, 0, false);
    }

    public MagicFromCardListChoice(final List<MagicCard> choiceList,final int amount) {
        this(choiceList, choiceList, amount, false);
    }
    
    public MagicFromCardListChoice(final List<MagicCard> choiceList,final int amount, final boolean upTo) {
        this(choiceList, choiceList, amount, upTo);
    }
    
    public MagicFromCardListChoice(final List<MagicCard> choiceList,final List<MagicCard> showList,final int amount) {
        this(choiceList, showList, amount, false);
    }
    public MagicFromCardListChoice(final List<MagicCard> aChoiceList,final List<MagicCard> aShowList,final int aAmount, final boolean aUpTo) {
        super(genDescription(aAmount));
        choiceList = aChoiceList;
        showList = new MagicCardList(aShowList);
        amount = aAmount;
        upTo = aUpTo;
    }

    private static final String genDescription(final int amount) {
        if (amount==1) {
            return "Choose a card.";
        } else {
            return "Choose " + amount + " cards.";
        }
    }

    private void createOptions(
            final Collection<Object> options,
            final List<MagicCard> cList,
            final MagicCard[] cards,
            final int count,
            final int aAmount,
            final int index) {

        if (count == aAmount) {
            options.add(new MagicCardChoiceResult(cards));
            return;
        }

        final int left = cList.size() - index;
        if (count + left < aAmount) {
            return;
        }

        cards[count]=cList.get(index);
        createOptions(options,cList,cards,count+1,aAmount,index+1);
        createOptions(options,cList,cards,count,aAmount,index+1);
    }

    // FIXME: need to implement upTo and ordering of cards for AI
    @Override
    Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {

        final List<Object> options = new ArrayList<Object>();
        final List<MagicCard> cList = new MagicCardList();
        
        //map the cards to the current game
        for (final MagicCard card : choiceList) {
            cList.add(card.map(game));
        }

        Collections.sort(cList);
        final int actualAmount = Math.min(amount,cList.size());
        if (actualAmount > 0) {
            createOptions(options,cList,new MagicCard[actualAmount],0,actualAmount,0);
        } else {
            options.add(new MagicCardChoiceResult());
        }
        return options;
    }

    @Override
    public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) throws UndoClickedException {

        final MagicCardChoiceResult result=new MagicCardChoiceResult();
        final Set<Object> validCards=new HashSet<Object>(choiceList);
        int actualAmount=Math.min(amount,validCards.size());
        if (actualAmount == 0) {
            final String message=result.size()>0?result.toString()+"|"+MESSAGE:MESSAGE;
            controller.showCards(showList);
            controller.focusViewers(5);
            controller.enableForwardButton();
            controller.waitForInput();
            controller.clearCards();
            controller.focusViewers(0);
            return new Object[]{result};
        } else {
            for (;actualAmount>0;actualAmount--) {
                final String message=result.size()>0?result.toString()+"|"+MESSAGE:MESSAGE;
                controller.showCards(showList);
                controller.focusViewers(5);
                controller.disableActionButton(false);
                controller.setValidChoices(validCards,false);
                controller.showMessage(source,message);
                if(upTo) controller.enableForwardButton();
                controller.waitForInput();
                if(controller.isActionClicked()) {
                    controller.clearCards();
                    controller.focusViewers(0);
                    return new Object[] {result};
                }
                final MagicCard card = controller.getChoiceClicked();
                validCards.remove(card);
                result.add(card);
            }
        }
        controller.clearCards();
        controller.focusViewers(0);
        return new Object[]{result};
    }
}
