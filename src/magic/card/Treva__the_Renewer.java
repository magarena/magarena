package magic.card;

import magic.model.MagicColor;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicColorChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


import java.util.Collection;

public class Treva__the_Renewer {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
			return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            "You may pay {2}{W}.",
                            new MagicPayManaCostChoice(MagicManaCost.TWO_WHITE),
                            MagicColorChoice.MOST_INSTANCE),
                        new Object[]{player},
                        this,
                        "You may$ pay {2}{W}$. If you do, choose a color$. " + 
                        player + " gains 1 life for each permanent of that color."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				int life=0;
				final MagicPlayer player=(MagicPlayer)data[0];
				final MagicColor color=(MagicColor)choiceResults[2];
				final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_PERMANENT);
				for (final MagicTarget target : targets) {
					final MagicPermanent permanent=(MagicPermanent)target;
					if (color.hasColor(permanent.getColorFlags())) {
						life++;
					}
				}				
				if (life>0) {
					game.doAction(new MagicChangeLifeAction(player,life));
				}
			}
		}		
    };
}
