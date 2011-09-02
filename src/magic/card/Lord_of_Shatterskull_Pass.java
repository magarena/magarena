package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

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
		
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player=permanent.getController();
			return (permanent==creature&&permanent.getCounters(MagicCounterType.Charge)>=6) ?
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
