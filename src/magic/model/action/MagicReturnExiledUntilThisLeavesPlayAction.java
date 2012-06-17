package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicReturnAuraEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicReturnExiledUntilThisLeavesPlayAction extends MagicAction {

    private final MagicPermanent source;
    private final MagicLocationType location;
    private MagicPlayer controller = MagicPlayer.NONE;
    private int action = MagicPlayCardAction.NONE;
    private MagicCardList exiledList;
    
    public MagicReturnExiledUntilThisLeavesPlayAction(
            final MagicPermanent source,
            final MagicLocationType location,
            final MagicPlayer controller,
            final int action) {
        this.source = source;
        this.location = location;
        this.controller = controller;
        this.action = action;
    }
    
    public MagicReturnExiledUntilThisLeavesPlayAction(
            final MagicPermanent source,
            final MagicLocationType location,
            final MagicPlayer controller) {
        this.source = source;
        this.location = location;
        this.controller = controller;
    }
    
    public MagicReturnExiledUntilThisLeavesPlayAction(
            final MagicPermanent source,
            final MagicLocationType location,
            final int action) {
        this.source = source;
        this.location = location;
        this.action = action;
    }
    
    public MagicReturnExiledUntilThisLeavesPlayAction(
            final MagicPermanent source,
            final MagicLocationType location) {
        this.source = source;
        this.location = location;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList cardList = source.getExiledCards();
        exiledList = new MagicCardList(source.getExiledCards());
            for (final MagicCard card : cardList) {
                if (card.getOwner().getExile().contains(card)) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Exile));
                    if (location == MagicLocationType.Play) {
                        if (card.getCardDefinition().isAura()) {
                            final MagicCardOnStack cardOnStack = new MagicCardOnStack(card,MagicPayedCost.NO_COST);
                            game.addEvent(new MagicReturnAuraEvent(cardOnStack));    
                        } else {
                            final Boolean newOwner = controller != MagicPlayer.NONE;
                            game.doAction(new MagicPlayCardAction(
                                    card,
                                    newOwner ? controller : card.getOwner(),
                                    action));
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
