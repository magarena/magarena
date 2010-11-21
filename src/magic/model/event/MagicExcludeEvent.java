package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.choice.MagicExcludeChoice;
import magic.model.choice.MagicExcludeResult;

public class MagicExcludeEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicExcludeResult result=(MagicExcludeResult)choiceResults[0];
			result.exclude(game);			
		}
	};
	
	public MagicExcludeEvent(final MagicPlayer player) {
		
		super(null,player,MagicExcludeChoice.getInstance(),MagicEvent.NO_DATA,EVENT_ACTION,null);
	}
}