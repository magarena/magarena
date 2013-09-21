package magic.model.action;

import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.stack.MagicCardOnStack;

public class MagicReturnExiledUntilThisLeavesPlayAction extends MagicAction {

    private final MagicPermanent source;
    private final MagicLocationType location;
    private final MagicPlayer controller;
    private final List<MagicPlayMod> modifications = new LinkedList<MagicPlayMod>();;
    private MagicCardList exiledList;
    
    public MagicReturnExiledUntilThisLeavesPlayAction(final MagicPermanent aSource, final MagicLocationType aLocation, final MagicPlayer aController) {
        source = aSource;
        location = aLocation;
        controller = aController;
    }
    
    public MagicReturnExiledUntilThisLeavesPlayAction(final MagicPermanent source, final MagicLocationType location) {
        this(source, location, MagicPlayer.NONE);
    }

    public MagicReturnExiledUntilThisLeavesPlayAction(final MagicPermanent aSource, final MagicLocationType aLocation, final MagicPlayer aController, final MagicPlayMod aModification) {
        this(aSource, aLocation, aController);
        modifications.add(aModification);
    }
    
    public MagicReturnExiledUntilThisLeavesPlayAction(final MagicPermanent source, final MagicLocationType location, final MagicPlayMod aModification) {
        this(source, location);
        modifications.add(aModification);
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList cardList = source.getExiledCards();
        exiledList = new MagicCardList(source.getExiledCards());
        for (final MagicCard card : cardList) {
            if (card.isInExile()) {
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Exile));
                if (location == MagicLocationType.Play) {
                    if (card.getCardDefinition().isAura()) {
                        final MagicCardOnStack cardOnStack = new MagicCardOnStack(card,MagicPayedCost.NOT_SPELL);
                        game.addEvent(cardOnStack.getEvent());
                    } else {
                        game.doAction(new MagicPlayCardAction(
                            card,
                            controller.isValid() ? controller : card.getOwner(),
                            modifications
                        ));
                    }
                } else {
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Exile,location));
                }
            }
        }
        cardList.clear();
    }

    @Override
    public void undoAction(final MagicGame game) {
        source.getExiledCards().addAll(exiledList);
    }
}
