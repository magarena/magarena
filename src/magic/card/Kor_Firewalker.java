
package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.*;

public class Kor_Firewalker {
    public static final MagicTrigger T=new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			final MagicCard card=((MagicCardOnStack)data).getCard();
			if (MagicColor.Red.hasColor(card.getColorFlags())) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "You gain 1 life.");
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
