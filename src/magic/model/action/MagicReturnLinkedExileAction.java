package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.stack.MagicCardOnStack;

import java.util.LinkedList;
import java.util.List;

public class MagicReturnLinkedExileAction extends MagicAction {

    private final MagicPermanent source;
    private final MagicLocationType location;
    private final MagicPlayer controller;
    private final List<MagicPermanentAction> modifications = new LinkedList<MagicPermanentAction>();;
    private MagicCardList exiledList;
    
    public MagicReturnLinkedExileAction(final MagicPermanent aSource, final MagicLocationType aLocation, final MagicPlayer aController) {
        source = aSource;
        location = aLocation;
        controller = aController;
    }
    
    public MagicReturnLinkedExileAction(final MagicPermanent source, final MagicLocationType location) {
        this(source, location, MagicPlayer.NONE);
    }

    public MagicReturnLinkedExileAction(final MagicPermanent aSource, final MagicLocationType aLocation, final MagicPlayer aController, final MagicPermanentAction aModification) {
        this(aSource, aLocation, aController);
        modifications.add(aModification);
    }
    
    public MagicReturnLinkedExileAction(final MagicPermanent source, final MagicLocationType location, final MagicPermanentAction aModification) {
        this(source, location);
        modifications.add(aModification);
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList cardList = source.getExiledCards();
        exiledList = new MagicCardList(source.getExiledCards());
        for (final MagicCard card : cardList) {
            if (card.isInExile()) {
                game.doAction(new RemoveCardAction(card,MagicLocationType.Exile));
                if (location == MagicLocationType.Play) {
                    game.doAction(new PlayCardAction(
                        card,
                        controller.isValid() ? controller : card.getOwner(),
                        modifications
                    ));
                } else {
                    game.doAction(new MoveCardAction(card,MagicLocationType.Exile,location));
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
