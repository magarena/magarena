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

public class Creeping_Tar_Pit {

	public static final MagicPermanentActivation V2944 =new MagicPermanentActivation(			"Creeping Tar Pit",
            new MagicCondition[]{new MagicArtificialCondition(
			    MagicManaCost.ONE_BLUE_BLACK.getCondition(),
                MagicManaCost.BLUE_BLUE_BLACK_BLACK.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_BLUE_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Until end of turn, Creeping Tar Pit becomes a 3/2 blue and black Elemental creature and is unblockable. It's still a land.");
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
			pt.power=3;
			pt.toughness=2;
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Unblockable.getMask();
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
			return MagicColor.Blue.getMask()|MagicColor.Black.getMask();
		}		
	}));
		}
	};
	

	
    public static final MagicTrigger V9907 =new MagicTappedIntoPlayTrigger("Creeping Tar Pit");

}
