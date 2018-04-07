package magic.model.stack;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCopyMap;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicObject;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MurmurHash3;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MoveCardAction;
import magic.model.event.MagicCardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceActivation;

public class MagicCardOnStack extends MagicItemOnStack implements MagicSource {

    private MagicLocationType moveLocation=MagicLocationType.Graveyard;
    private MagicLocationType fromLocation=MagicLocationType.OwnersHand;
    private final MagicPayedCost payedCost;
    private final MagicCardEvent cardEvent;
    private final MagicEvent event;
    private final MagicCardDefinition cardDef;
    private final List<? extends MagicPermanentAction> modifications;

    public MagicCardOnStack(
        final MagicCard card,
        final MagicObject obj,
        final MagicPlayer controller,
        final MagicCardEvent aCardEvent,
        final MagicPayedCost aPayedCost,
        final List<? extends MagicPermanentAction> aModifications
    ) {
        super(card, controller);
        payedCost = aPayedCost;
        cardEvent = aCardEvent;
        cardDef = obj.getCardDefinition();
        event = aCardEvent.getEvent(this, aPayedCost);
        assert event != MagicEvent.NONE : "event is NONE for " + cardDef;
        modifications = aModifications;
    }

    public MagicCardOnStack(final MagicCard card,final MagicCardEvent aCardEvent, final MagicPayedCost aPayedCost) {
        this(card, card, card.getController(), aCardEvent, aPayedCost, Collections.<MagicPermanentAction>emptyList());
    }

    public MagicCardOnStack(final MagicCard card,final MagicPlayer controller,final MagicPayedCost aPayedCost) {
        this(card, card, controller, card.getCardDefinition().getCardEvent(), aPayedCost, Collections.<MagicPermanentAction>emptyList());
    }

    public MagicCardOnStack(final MagicCard card,final MagicPlayer controller,final MagicPayedCost aPayedCost, final List<? extends MagicPermanentAction> aModifications) {
        this(card, card, controller, card.getCardDefinition().getCardEvent(), aPayedCost, aModifications);
    }

    public MagicCardOnStack(final MagicCard card,final MagicPayedCost aPayedCost) {
        this(card, card.getCardDefinition().getCardEvent(), aPayedCost);
    }

    private MagicCardOnStack(final MagicCopyMap copyMap, final MagicCardOnStack cardOnStack) {
        super(copyMap, cardOnStack);
        payedCost = copyMap.copy(cardOnStack.payedCost);
        moveLocation = cardOnStack.moveLocation;
        fromLocation = cardOnStack.fromLocation;
        cardEvent = cardOnStack.cardEvent;
        cardDef = cardOnStack.cardDef;
        event = copyMap.copy(cardOnStack.event);
        modifications = cardOnStack.modifications;
    }

    public MagicCardOnStack copyCardOnStack(final MagicPlayer player) {
        final MagicCard card=MagicCard.createTokenCard(getCardDefinition(),player);
        final MagicCardOnStack copyCardOnStack=new MagicCardOnStack(card,cardEvent,payedCost);
        final Object[] choiceResults=getChoiceResults();
        if (choiceResults!=null) {
            copyCardOnStack.setChoiceResults(Arrays.copyOf(choiceResults,choiceResults.length));
        }
        return copyCardOnStack;
    }

    @Override
    public long getStateId() {
        return MurmurHash3.hash(new long[] {
            super.getStateId(),
            moveLocation.ordinal(),
            fromLocation.ordinal(),
            payedCost.getStateId(),
            cardDef.getIndex()
        });
    }

    @Override
    public MagicCardDefinition getCardDefinition() {
        return cardDef;
    }

    @Override
    public MagicEvent getEvent() {
        return event;
    }

    @Override
    public void resolve(final MagicGame game) {
        super.resolve(game);
        // Move card to move location that is not play
        if (moveLocation != MagicLocationType.Battlefield) {
            game.doAction(new MoveCardAction(this));
        }
    }

    @Override
    public MagicCardOnStack copy(final MagicCopyMap copyMap) {
        return new MagicCardOnStack(copyMap, this);
    }

    @Override
    public MagicCardOnStack map(final MagicGame game) {
        return (MagicCardOnStack)super.map(game);
    }

    public MagicCard getCard() {
        return (MagicCard)getSource();
    }

    public void setMoveLocation(final MagicLocationType loc) {
        moveLocation = loc;
    }

    public MagicLocationType getMoveLocation() {
        return moveLocation;
    }

    public void setFromLocation(final MagicLocationType loc) {
        fromLocation = loc;
    }

    public MagicLocationType getFromLocation() {
        return fromLocation;
    }

    public List<? extends MagicPermanentAction> getModifications() {
        return modifications;
    }

    @Override
    public int getConvertedCost() {
        return getCardDefinition().getConvertedCost(payedCost.getX());
    }

    public int getX() {
        return payedCost.getX();
    }

    public MagicPayedCost getPayedCost() {
        return payedCost;
    }

    @Override
    public boolean isSpell() {
        return true;
    }

    public boolean isCast() {
        return payedCost != MagicPayedCost.NOT_SPELL;
    }

    public boolean isRepresentedByACard() {
        return !getCard().isToken();
    }

    @Override
    public boolean canBeCountered() {
        return !getCardDefinition().hasAbility(MagicAbility.CannotBeCountered);
    }

    @Override
    public MagicGame getGame() {
        return getSource().getGame();
    }

    @Override
    public Collection<MagicSourceActivation<? extends MagicSource>> getSourceActivations() {
        return Collections.emptyList();
    }

    public int getKicker() {
        return payedCost.getKicker();
    }

    public boolean isKicked() {
        return payedCost.isKicked();
    }

    public boolean isFaceDown() {
        return false;
    }
}
