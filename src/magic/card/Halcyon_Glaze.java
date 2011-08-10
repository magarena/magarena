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

public class Halcyon_Glaze {

    public static final MagicTrigger V10093 =new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Halcyon Glaze") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data;
			if (cardOnStack.getController()==player&&cardOnStack.getCardDefinition().isCreature()) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,
					"Halcyon Glaze becomes a 4/4 Illusion creature with flying until end of turn. It's still an enchantment.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],
	new MagicDummyLocalVariable() {
		
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=4;
			pt.toughness=4;
		}

		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Flying.getMask();
		}

		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.add(MagicSubType.Illusion);
			return mod;
		}
		
        @Override
		public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
	}));
		}
    };
    
}
