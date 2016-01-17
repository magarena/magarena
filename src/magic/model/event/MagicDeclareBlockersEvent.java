package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.DeclareBlockersAction;
import magic.model.choice.MagicDeclareBlockersChoice;
import magic.model.choice.MagicDeclareBlockersResult;

public class MagicDeclareBlockersEvent extends MagicEvent {

    public MagicDeclareBlockersEvent(final MagicPlayer player) {
        super(
            MagicSource.NONE,
            player,
            MagicDeclareBlockersChoice.getInstance(),
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicPlayer player = event.getPlayer();
        final MagicDeclareBlockersResult result = event.getBlockers();
        game.doAction(new DeclareBlockersAction(player,result));
        game.logBlockers(player,result);
    };
}
