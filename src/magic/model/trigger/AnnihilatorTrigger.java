package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;

public class AnnihilatorTrigger extends MagicWhenAttacksTrigger {
    private final int amount;

    public AnnihilatorTrigger(final int amount) {
        this.amount = amount;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent creature) {
        final MagicPlayer opponent = permanent.getOpponent();
        return (permanent == creature) ?
            new MagicEvent(
                permanent,
                opponent,
                this,
                "PN sacrifices " + amount + (amount == 1 ? " permanent." : " permanents.")
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        int count = amount;
        while (count > 0 && player.getPermanents().size() > 0) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                player,
                MagicTargetChoice.SACRIFICE_PERMANENT
            ));
            count--;
        }
    }
}
