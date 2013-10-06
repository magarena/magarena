package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;

public class MagicThiefTrigger extends MagicWhenDamageIsDealtTrigger {

    public static enum Choice {
        May,
        Must
    }

    public static enum Type {
        Combat,
        Any
    }

    public static enum Player {
        Opponent,
        Any
    }

    private final Type type;
    private final Choice choice;
    private final Player player;
    private final int amount = 1;

    public MagicThiefTrigger(final Type type,final Choice choice,final Player player) {
        this.type = type;
        this.choice = choice;
        this.player = player;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        if (damage.getSource() == permanent &&
            damage.getTarget().isPlayer() &&
            (player == Player.Any || permanent.isOpponent(damage.getTarget())) &&
            (type == Type.Any || damage.isCombat())) {
            return (choice == Choice.Must) ?
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
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (choice == Choice.Must || event.isYes()) {
            game.doAction(new MagicDrawAction(event.getPlayer(),amount));
        }
    }
}
