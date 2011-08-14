package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

import java.util.Collection;

public class Lord_of_Shatterskull_Pass {
    private static final MagicLocalVariable LV = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (permanent.getCounters(MagicCounterType.Charge)>0) {
				pt.power=6;
				pt.toughness=6;
			} 
		}		
	};

	public static final MagicPermanentActivation A = new MagicLevelUpActivation(MagicManaCost.ONE_RED,6);
		
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(LV);	
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.setVariablePT();
        }
    };
		
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
            final MagicPlayer player=permanent.getController();
			return (permanent==data&&permanent.getCounters(MagicCounterType.Charge)>=6) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,game.getOpponent(player)},
                        this,
                        permanent + " deals 6 damage to each creature defending player controls."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicSource source=(MagicSource)data[0];
			final MagicPlayer defendingPlayer=(MagicPlayer)data[1];
			final Collection<MagicTarget> creatures=
                game.filterTargets(defendingPlayer,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget creature : creatures) {
				final MagicDamage damage=new MagicDamage(source,creature,6,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
}
