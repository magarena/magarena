package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicDeclareAttackersAction;
import magic.model.choice.MagicDeclareAttackersChoice;
import magic.model.choice.MagicDeclareAttackersResult;

public class MagicDeclareAttackersEvent extends MagicEvent {
	
	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			final MagicDeclareAttackersResult result=(MagicDeclareAttackersResult)choiceResults[0];
			game.doAction(new MagicDeclareAttackersAction(player,result));
			game.logAttackers(player,result);
		}
	};
	
	public MagicDeclareAttackersEvent(final MagicPlayer player) {
		
		super(null,player,MagicDeclareAttackersChoice.getInstance(),new Object[]{player},EVENT_ACTION,null);
	}
}