package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDeclareAttackersAction;
import magic.model.choice.MagicDeclareAttackersChoice;
import magic.model.choice.MagicDeclareAttackersResult;

public class MagicDeclareAttackersEvent extends MagicEvent {

    public MagicDeclareAttackersEvent(final MagicPlayer player) {
        super(
            MagicSource.NONE,
            player,
            MagicDeclareAttackersChoice.getInstance(),
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicDeclareAttackersResult result = event.getAttackers();
            game.doAction(new MagicDeclareAttackersAction(player,result));
            game.logAttackers(player,result);
        }
    };
}
