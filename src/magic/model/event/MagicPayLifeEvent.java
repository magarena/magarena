package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeLifeAction;

public class MagicPayLifeEvent extends MagicEvent {
    public MagicPayLifeEvent(final MagicSource source,final MagicPlayer player,final int amount) {
        super(
            source,
            player,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),-amount));
                }
            },
            "Pay "+amount+" life."
        );        
    }    
}
