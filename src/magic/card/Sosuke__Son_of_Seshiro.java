package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanentState;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Sosuke__Son_of_Seshiro {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.ModPT, 
			MagicTargetFilter.TARGET_SNAKE_YOU_CONTROL) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(1,0);
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return source != target;
		}
	};
	    
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			return (damage.getSource().getController() == permanent.getController() &&
                    damage.getSource().isPermanent() &&
					((MagicPermanent)damage.getSource()).hasSubType(MagicSubType.Warrior,game) &&
                    damage.isCombat() &&
					damage.getTarget().isPermanent() &&
					((MagicPermanent)damage.getTarget()).isCreature(game)) ?
                new MagicEvent(
                		damage.getSource(),
                        permanent.getController(),
                        new Object[]{damage.getTarget()},
                        this,
                        "Destroy " + damage.getTarget() + " at end of combat."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeStateAction((MagicPermanent)data[0],MagicPermanentState.DestroyAtEndOfCombat,true));
        }
    };
}
