package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

import java.util.Collection;

public class Call_to_the_Grave {
	public static final MagicAtUpkeepTrigger T1 = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
				return new MagicEvent(
						permanent,
						player,
						new Object[]{permanent,player},
						this,
						player + " sacrifices a non-Zombie creature.");
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[1];
			if (player.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicSacrificePermanentEvent(
                            (MagicPermanent)data[0],
                            player,
                            MagicTargetChoice.SACRIFICE_NON_ZOMBIE));
			}
		}
    };
    
    public static final MagicAtEndOfTurnTrigger T2 = new MagicAtEndOfTurnTrigger() {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
    		final Collection<MagicTarget> targets =
                    game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_CREATURE);
    		if (targets.size() == 0) {
    			return new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        "Sacrifice " + permanent + ".");
    			
    		}
    		return null;
    	}
    	
    	@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
    		game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
		}	
    };
}
