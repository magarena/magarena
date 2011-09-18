package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

import java.util.Arrays;
import java.util.EnumSet;

public class Stirring_Wildwood {
	
    private static final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
			pt.set(3,4);
		}
    };
    private static final MagicStatic AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
		@Override
		public long getAbilityFlags(
                final MagicGame game,
                final MagicPermanent permanent,
                final long flags) {
			return flags|MagicAbility.Reach.getMask();
		}
    };
    private static final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(
                final MagicPermanent permanent,
                final EnumSet<MagicSubType> flags) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.add(MagicSubType.Elemental);
            return mod;
		}
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
    };
    private static final MagicStatic C = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return MagicColor.Green.getMask()|MagicColor.White.getMask();
		}		
	};

	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{new MagicArtificialCondition(
					MagicManaCost.ONE_GREEN_WHITE.getCondition(),
                    MagicManaCost.GREEN_GREEN_WHITE_WHITE.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),
                MagicManaCost.ONE_GREEN_WHITE)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},this,
                    "Until end of turn, " + source + 
                    " becomes a 3/4 green and white Elemental creature with reach. It's still a land.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],PT,AB,ST,C));
		}
	};
	
    public static final MagicTrigger T = new MagicTappedIntoPlayTrigger();
    
    public static final MagicManaActivation M = new MagicTapManaActivation(
            Arrays.asList(MagicManaType.Green,MagicManaType.White), 1);
    
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(final MagicCardDefinition cdef) {
		    cdef.setExcludeManaOrCombat();
        }
    };
}
