package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.stack.MagicCardOnStack;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.action.RemoveTriggerAction;
import magic.model.action.CastCardAction;

public class MagicReboundTrigger extends MagicAtUpkeepTrigger {

    private final MagicCard staleCard;

    public MagicReboundTrigger(final MagicCard card) {
        staleCard = card;
    }
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        return staleCard.getOwner().getId() == upkeepPlayer.getId();
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent none,final MagicPlayer upkeepPlayer) {
        game.addDelayedAction(new RemoveTriggerAction(this));
        final MagicCard mappedCard = upkeepPlayer.getExile().getCard(staleCard.getId());
        return mappedCard.isInExile() ?
            new MagicEvent(
                mappedCard,
                upkeepPlayer,
                new MagicMayChoice(),
                this,
                "You may$ cast SN without paying its mana costs."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes() && event.getCard().isInExile()) {
            game.doAction(CastCardAction.WithoutManaCost(
                event.getPlayer(),
                event.getCard(),
                MagicLocationType.Exile,
                MagicLocationType.Graveyard
            ));
        }
    }
}
