package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;


import java.util.Collection;

public class Murkfiend_Liege {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player=permanent.getController();
			return (player!=data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "Untap all green and/or blue creatures you control."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final Collection<MagicTarget> targets=
                game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				final MagicPermanent creature=(MagicPermanent)target;
				final int colorFlags=creature.getColorFlags();
				if (creature.isTapped()&&(MagicColor.Blue.hasColor(colorFlags)||MagicColor.Green.hasColor(colorFlags))) {
					game.doAction(new MagicUntapAction(creature));
				}
			}
		}
		@Override
		public boolean usesStack() {
			return false;
		}
    };
}
