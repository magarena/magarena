
package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Kor_Firewalker {
    public static final MagicTrigger T=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack data) {
			final MagicPlayer player=permanent.getController();
			final MagicCard card=data.getCard();
			if (MagicColor.Red.hasColor(card.getColorFlags())) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " gains 1 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			if (player!=null) {
			    game.doAction(new MagicChangeLifeAction(player,1));
			}
		}		
    };
}
