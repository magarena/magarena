package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
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
            final MagicPlayer player = permanent.getController();
            if (mustDraw) {
                return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        (amount > 1) ?
                            player + " draws " + amount + " cards." :
                            player + " draws a card.");
            }
            else {
                return new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may draw a card.",
                                MagicSimpleMayChoice.DRAW_CARDS,
                                amount,
                                MagicSimpleMayChoice.DEFAULT_NONE),
                        new Object[]{player},
                        this,
                        (amount > 1) ?
                                player + " may$ draw " + amount + " cards." :
                                player + " may$ draw a card.");
            }
        }
        return MagicEvent.NONE;
    }

    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        if (mustDraw) {
            game.doAction(new MagicDrawAction((MagicPlayer)data[0],amount));
        } else if (MagicMayChoice.isYesChoice(choiceResults[0])) {
            game.doAction(new MagicDrawAction((MagicPlayer)data[0],amount));
        }
    }
}
