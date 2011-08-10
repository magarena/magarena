package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Celestial_Colonnade {

	public static final MagicPermanentActivation V2886 =new MagicPermanentActivation(			"Celestial Colonnade",
            new MagicCondition[]{new MagicArtificialCondition(
                MagicManaCost.THREE_WHITE_BLUE.getCondition(),
                MagicManaCost.TWO_WHITE_WHITE_BLUE_BLUE.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_WHITE_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, Celestial Colonnade becomes a 4/4 white and blue Elemental creature with flying and vigilance. It's still a land.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],
	new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=4;
			pt.toughness=4;
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Flying.getMask()|MagicAbility.Vigilance.getMask();
		}
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.add(MagicSubType.Elemental);
			return mod;
		}
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return MagicColor.White.getMask()|MagicColor.Blue.getMask();
		}		
	}));
		}
	};


    public static final MagicTrigger V9899 =new MagicTappedIntoPlayTrigger("Celestial Colonnade");

}
