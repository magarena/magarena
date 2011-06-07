package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.*;

public class Celestial_Purge {
    public static final MagicSpellCardEvent EXILE=new MagicSpellCardEvent() {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.NEG_TARGET_BLACK_RED_PERMANENT,
                    MagicExileTargetPicker.getInstance(),
                    new Object[]{cardOnStack},
                    this,				
                    "Exile target black or red permanent$.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent perm=event.getTarget(game,choiceResults,0);
			if (perm!=null) {
				game.doAction(new MagicRemoveFromPlayAction(perm,MagicLocationType.Exile));
			}
		}
	};
}
