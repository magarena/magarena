package magic.model.action;

import java.util.LinkedList;
import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class ReturnLinkedExileAction extends MagicAction {

    private final MagicPermanent source;
    private final MagicLocationType location;
    private final MagicPlayer controller;
    private final List<MagicPermanentAction> modifications = new LinkedList<>();
    private MagicCardList exiledList;

    public ReturnLinkedExileAction(final MagicPermanent aSource, final MagicLocationType aLocation, final MagicPlayer aController) {
        source = aSource;
        location = aLocation;
        controller = aController;
    }

    public ReturnLinkedExileAction(final MagicPermanent source, final MagicLocationType location) {
        this(source, location, MagicPlayer.NONE);
    }

    public ReturnLinkedExileAction(final MagicPermanent aSource, final MagicLocationType aLocation, final MagicPlayer aController, final MagicPermanentAction aModification) {
        this(aSource, aLocation, aController);
        modifications.add(aModification);
    }

    public ReturnLinkedExileAction(final MagicPermanent source, final MagicLocationType location, final MagicPermanentAction aModification) {
        this(source, location);
        modifications.add(aModification);
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicCardList cardList = source.getExiledCards();
        exiledList = new MagicCardList(source.getExiledCards());
        for (final MagicCard card : cardList) {
            if (card.isInExile()) {
                if (location == MagicLocationType.Battlefield) {
                    game.doAction(new PutOntoBattlefieldAction(
                        MagicLocationType.Exile,
                        card,
                        controller.isValid() ? controller : card.getOwner(),
                        modifications
                    ));
                } else {
                    game.doAction(new ShiftCardAction(card,MagicLocationType.Exile,location));
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
