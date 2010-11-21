package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicDeclareBlockersAction;
import magic.model.choice.MagicDeclareBlockersChoice;
import magic.model.choice.MagicDeclareBlockersResult;

public class MagicDeclareBlockersEvent extends MagicEvent {
	
	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			final MagicDeclareBlockersResult result=(MagicDeclareBlockersResult)choiceResults[0];
			game.doAction(new MagicDeclareBlockersAction(result));
			game.logBlockers(player,result);
		}
	};
	
	public MagicDeclareBlockersEvent(final MagicPlayer player) {
		
		super(null,player,MagicDeclareBlockersChoice.getInstance(),new Object[]{player},EVENT_ACTION,null);
	}
}