package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;

public class MagicRefugeLandTrigger extends MagicWhenComesIntoPlayTrigger {
    private final int life;

    public MagicRefugeLandTrigger(final int amount) {
        life = amount;
    }
    
    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent, 
            final MagicPlayer player) {
        return new MagicEvent(
            permanent,
            player,
            this,
            player + 
            (life > 0 ? " gains " + life : " loses " + -life) + 
            " life."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeLifeAction(event.getPlayer(),life));
    }
}
