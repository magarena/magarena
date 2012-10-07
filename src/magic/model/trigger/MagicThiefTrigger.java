package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;

public class MagicThiefTrigger extends MagicWhenDamageIsDealtTrigger {

    private final boolean combat;
    private final boolean mustDraw;
    private final int amount;
    
    public MagicThiefTrigger(final boolean combat,final boolean mustDraw,final int amount) {
        this.combat = combat;
        this.mustDraw = mustDraw;
        this.amount = amount;
    }
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        if (damage.getSource() == permanent &&
            damage.getTarget().isPlayer() &&
            (!combat || damage.isCombat())) {
            return (mustDraw) ?
                new MagicEvent(
                    permanent,
                    this,
                    (amount > 1) ?
                        "PN draws " + amount + " cards." :
                        "PN draws a card."
                ):
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        amount,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    (amount > 1) ?
                        "PN may$ draw " + amount + " cards." :
                        "PN may$ draw a card."
                );
        }
        return MagicEvent.NONE;
    }

    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        if (mustDraw) {
            game.doAction(new MagicDrawAction(event.getPlayer(),amount));
        } else if (MagicMayChoice.isYesChoice(choiceResults[0])) {
            game.doAction(new MagicDrawAction(event.getPlayer(),amount));
        }
    }
}
