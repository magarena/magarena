package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class MoveCardAction extends MagicAction {

    public final MagicCard card;
    public final MagicPermanent permanent;
    public final MagicLocationType fromLocation;
    private MagicLocationType toLocation;

    private MoveCardAction(final MagicCard card, final MagicPermanent permanent, final MagicLocationType fromLocation, final MagicLocationType toLocation) {
        this.card=card;
        this.permanent=permanent;
        this.fromLocation=fromLocation;
        this.toLocation=toLocation;
    }

    public MoveCardAction(final MagicCard card,final MagicLocationType fromLocation,final MagicLocationType toLocation) {
        this(card,MagicPermanent.NONE,fromLocation,toLocation);
    }

    public MoveCardAction(final MagicPermanent permanent,final MagicLocationType toLocation) {
        this(permanent.getCard(),permanent,MagicLocationType.Battlefield,toLocation);
    }

    public MoveCardAction(final MagicCardOnStack cardOnStack) {
        this(cardOnStack.getCard(),MagicPermanent.NONE,MagicLocationType.Stack,cardOnStack.getMoveLocation());
    }

    public boolean from(MagicLocationType loc) {
        return fromLocation == loc;
    }

    public boolean to(MagicLocationType loc) {
        return toLocation == loc;
    }

    public void setToLocation(final MagicLocationType aToLocation) {
        toLocation = aToLocation;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.executeTrigger(MagicTriggerType.WouldBeMoved, this);

        final MagicSource triggerSource = permanent.isValid() ? permanent : card;

        // Execute replacement triggers
        for (final MagicTrigger<MoveCardAction> trigger : card.getCardDefinition().getPutIntoGraveyardTriggers()) {
            if (toLocation == MagicLocationType.Graveyard && trigger.getPriority() == MagicTrigger.REPLACEMENT) {
                game.executeTrigger(trigger, permanent, triggerSource, this);
            }
        }

        // Move card
        if (!card.isToken()) {
            final MagicPlayer owner=card.getOwner();
            switch (toLocation) {
                case TopOfOwnersLibrary:
                    owner.getLibrary().addToTop(card);
                    break;
                case BottomOfOwnersLibrary:
                    owner.getLibrary().addToBottom(card);
                    break;
                case OwnersLibrary:
                    owner.getLibrary().addToTop(card);
                    game.doAction(new ShuffleLibraryAction(owner));
                    break;
                case OwnersHand:
                    owner.addCardToHand(card);
                    setScore(owner,ArtificialScoringSystem.getCardScore(card));
                    break;
                case Graveyard:
                    owner.getGraveyard().addToTop(card);
                    break;
                case Exile:
                    owner.getExile().addToTop(card);
                    break;
                default: throw new RuntimeException("Unsupported location for MoveCardAction: " + toLocation);
            }
        }

        // Execute triggers
        for (final MagicTrigger<MoveCardAction> trigger : card.getCardDefinition().getPutIntoGraveyardTriggers()) {
            if (toLocation == MagicLocationType.Graveyard && trigger.getPriority() > MagicTrigger.REPLACEMENT) {
                game.executeTrigger(trigger, permanent, triggerSource, this);
            }
        }

        if (toLocation == MagicLocationType.Graveyard) {
            game.executeTrigger(MagicTriggerType.WhenOtherPutIntoGraveyard, this);
        }

        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (!card.isToken()) {
            final MagicPlayer owner=card.getOwner();
            switch (toLocation) {
                case TopOfOwnersLibrary:
                case BottomOfOwnersLibrary:
                case OwnersLibrary:
                    owner.getLibrary().remove(card);
                    break;
                case OwnersHand:
                    owner.removeCardFromHand(card);
                    break;
                case Graveyard:
                    owner.getGraveyard().remove(card);
                    break;
                case Exile:
                    owner.getExile().remove(card);
                    break;
                default: throw new RuntimeException("Unsupported location for MoveCardAction: " + toLocation);
            }
        }
    }

    @Override
    public String toString() {
        return super.toString()+" ("+card+','+fromLocation+','+toLocation+')';
    }
}
