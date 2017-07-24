package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicEvent;
import magic.model.action.AIRevealAction;
import magic.model.action.CastCardAction;
import magic.model.action.EnqueueTriggerAction;

public class MiracleTrigger extends ThisDrawnTrigger {

    private final MagicManaCost manaCost;

    public MiracleTrigger(final MagicManaCost aManaCost) {
        manaCost = aManaCost;
    }

    @Override
    public boolean usesStack() {
        return false;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent none,final MagicCard card) {
        final MagicPlayer player = card.getOwner();
        return player.getDrawnCards() == 1 ?
            new MagicEvent(
                player.isHuman() ? card : MagicSource.NONE,
                player,
                new MagicMayChoice(
                    player.isHuman() ? "Reveal this card and cast it for its miracle cost?" : "..."
                ),
                card,
                this::executeEvent,
                ""
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getRefCard();
        if (event.isYes() && card.isInHand()) {
            game.doAction(new AIRevealAction(card));
            game.doAction(new EnqueueTriggerAction(new MagicEvent(
                card,
                card.getOwner(),
                card,
                this::executeTriggerEvent,
                "PN reveals SN and casts it by paying " + game.modCost(card, manaCost) + "."
            )));
        }
    }
    private void executeTriggerEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getRefCard();
        final MagicEvent cost = new MagicPayManaCostEvent(card, game.modCost(card, manaCost));
        if (card.isInHand() && cost.isSatisfied()) {
            game.addCostEvent(cost);
            game.doAction(CastCardAction.WithoutManaCost(
                event.getPlayer(),
                card,
                MagicLocationType.OwnersHand,
                MagicLocationType.Graveyard
            ));
        }
    }
}
