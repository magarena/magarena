package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Mikaeus__the_Unhallowed {
	public static final MagicStatic S1 = new MagicStatic(
			MagicLayer.ModPT, 
			MagicTargetFilter.TARGET_NONHUMAN_CREATURE_YOU_CONTROL) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(1,1);
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return source != target;
		}
	};

	public static final MagicStatic S2 = new MagicStatic(
			MagicLayer.Ability, 
			MagicTargetFilter.TARGET_NONHUMAN_CREATURE_YOU_CONTROL) {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags | MagicAbility.Undying.getMask();
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return source != target;
		}
	};
	    
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicDamage damage) {
			final MagicPlayer player = permanent.getController();
			return (damage.getTarget() == player && 
                    damage.getSource().isPermanent() &&
                    ((MagicPermanent)damage.getSource()).hasSubType(MagicSubType.Human)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{damage.getSource()},
                        this,
                        "Destroy " + damage.getSource() + "."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicDestroyAction((MagicPermanent)data[0]));
		}
    };
}
