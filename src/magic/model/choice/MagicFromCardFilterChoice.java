package magic.model.choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import magic.exception.UndoClickedException;
import magic.model.IUIGameController;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;

public class MagicFromCardFilterChoice extends MagicChoice {

    private final MagicTargetFilter<MagicCard> filter;
    private final int amount;
    private final boolean upTo;

    public MagicFromCardFilterChoice(final MagicTargetFilter<MagicCard> aFilter, final int aAmount, final boolean aUpTo, final String description) {
        super(genDescription(aAmount, description, aUpTo));
        filter = aFilter;
        amount = aAmount;
        upTo = aUpTo;
    }

    private static final String genDescription(final int amount, final String description, final boolean aUpTo) {
        final String paddedDesc = description.isEmpty() ? description : " " + description;

        if (aUpTo && amount == 1) {
            return "Choose up to 1 card" + paddedDesc + ".";
        } else if (aUpTo && amount != 1) {
            return "Choose up to " + amount + " cards" + paddedDesc + ".";
        } else if (!aUpTo && amount == 1) {
            return "Choose a card" + paddedDesc + ".";
        } else {
            return "Choose " + amount + " cards" + paddedDesc + ".";
        }
    }

    private void createOptions(
        final Collection<Object> options,
        final List<MagicCard> cList,
        final MagicCard[] cards,
        final int count,
        final int limit,
        final int index
    ) {

        if (count == limit) {
            options.add(new MagicCardChoiceResult(cards));
            return;
        }

        final int left = cList.size() - index;
        if (count + left < limit) {
            return;
        }

        cards[count] = cList.get(index);
        createOptions(options, cList, cards, count + 1, limit, index + 1);
        createOptions(options, cList, cards, count, limit, index + 1);
    }

    private void createOptionsUpTo(
        final Collection<Object> options,
        final List<MagicCard> cList,
        final MagicCard[] cards,
        final int count,
        final int limit,
        final int index
    ) {

        if (index >= cList.size() || count >= limit) {
            final MagicCardChoiceResult result = new MagicCardChoiceResult(cards);
            //System.out.println("add " + result);
            options.add(result);
        } else {
            final MagicCard first = cList.get(index);
            int cnt = 0;
            for (int i = 0; index + i < cList.size() && cList.get(index + i).getStateId() == first.getStateId(); i++) {
                cnt++;
            }

            // use 1 to cnt copies of first
            for (int i = 0; i < cnt && count + i + 1 <= limit; i++) {
                cards[count + i] = cList.get(index + i);
                createOptionsUpTo(options, cList, cards, count + i + 1, limit, index + cnt);
            }

            // use 0 copies of first
            for (int i = 0; i < cnt && count + i + 1 <= limit; i++) {
                cards[count + i] = null;
            }

            createOptionsUpTo(options, cList, cards, count, limit, index + cnt);
        }
    }

    // FIXME: need to implement ordering of cards for AI, needed by scry
    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final List<Object> options = new ArrayList<Object>();
        final List<MagicCard> oList = player.filterCards(filter);
        final List<Boolean> known = new ArrayList<Boolean>(oList.size());

        //reveal the cards
        for (final MagicCard card : oList) {
            known.add(card.isGameKnown());
            card.setGameKnown(true);
        }

        final List<MagicCard> cList = new MagicCardList(oList);
        Collections.sort(cList);

        final int actualAmount = Math.min(amount, cList.size());
        if (actualAmount == 0) {
            options.add(new MagicCardChoiceResult());
        } else if (upTo) {
            createOptionsUpTo(options, cList, new MagicCard[actualAmount], 0, actualAmount, 0);
        } else {
            createOptions(options, cList, new MagicCard[actualAmount], 0, actualAmount, 0);
        }

        //hide the cards
        for (int i = 0; i < oList.size(); i++) {
            oList.get(i).setGameKnown(known.get(i));
        }

        return options;
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final MagicCardChoiceResult result = new MagicCardChoiceResult();
        final List<MagicCard> choiceList = player.filterCards(filter);
        final MagicCardList showList = new MagicCardList(choiceList);
        final Set<Object> validCards = new HashSet<Object>(choiceList);
        int actualAmount = Math.min(amount, validCards.size());
        if (actualAmount == 0) {
            controller.showCards(showList);
            controller.focusViewers(5);
            controller.enableForwardButton();
            controller.waitForInput();
            controller.clearCards();
            controller.focusViewers(0);
            return new Object[]{result};
        } else {
            for (; actualAmount > 0; actualAmount--) {
                final String message = result.size() > 0 ? result.toString() + "|" + getDescription() : getDescription();
                controller.showCards(showList);
                controller.focusViewers(5);
                controller.disableActionButton(false);
                controller.setValidChoices(validCards, false);
                controller.showMessage(source, message);
                if (upTo) {
                    controller.enableForwardButton();
                }
                controller.waitForInput();
                if (controller.isActionClicked()) {
                    controller.clearCards();
                    controller.focusViewers(0);
                    return new Object[]{result};
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
