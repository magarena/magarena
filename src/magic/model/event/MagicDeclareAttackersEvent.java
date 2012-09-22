package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicDeclareAttackersAction;
import magic.model.choice.MagicDeclareAttackersChoice;
import magic.model.choice.MagicDeclareAttackersResult;

public class MagicDeclareAttackersEvent extends MagicEvent {
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            final MagicDeclareAttackersResult result = (MagicDeclareAttackersResult)choiceResults[0];
            game.doAction(new MagicDeclareAttackersAction(player,result));
            game.logAttackers(player,result);
        }
    };
    
    public MagicDeclareAttackersEvent(final MagicPlayer player) {
        super(
            MagicEvent.NO_SOURCE,
            player,
            MagicDeclareAttackersChoice.getInstance(),
            EVENT_ACTION,
            ""
        );
    }
}
