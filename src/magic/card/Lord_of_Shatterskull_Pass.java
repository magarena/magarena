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

public class Lord_of_Shatterskull_Pass {
	
    private static final MagicLocalVariable LORD_OF_SHATTERSKULL_PASS = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (permanent.getCounters(MagicCounterType.Charge)>0) {
				pt.power=6;
				pt.toughness=6;
			} 
		}		
	};

	public static final MagicPermanentActivation V1260 = new MagicLevelUpActivation("Lord of Shatterskull Pass",MagicManaCost.ONE_RED,6);
		
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(LORD_OF_SHATTERSKULL_PASS);	
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.setVariablePT();
        }
    };
		
    public static final MagicTrigger V7917 = new MagicTrigger(MagicTriggerType.WhenAttacks,"Lord of Shatterskull Pass") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data&&permanent.getCounters(MagicCounterType.Charge)>=6) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{permanent,game.getOpponent(player)},this,
					"Lord of Shatterskull Pass deals 6 damage to each creature defending player controls.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicSource source=(MagicSource)data[0];
			final MagicPlayer defendingPlayer=(MagicPlayer)data[1];
			final Collection<MagicTarget> creatures=game.filterTargets(defendingPlayer,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget creature : creatures) {
				
				final MagicDamage damage=new MagicDamage(source,creature,6,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
}
