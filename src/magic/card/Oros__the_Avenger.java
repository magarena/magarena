package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

public class Oros__the_Avenger {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenDamageIsDealt) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();		
			return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            "You may pay {2}{W}.",
                            new MagicPayManaCostChoice(MagicManaCost.TWO_WHITE)),
                        new Object[]{player,permanent},
                        this,
                        "You may$ pay {2}{W}$. If you do, " + permanent + 
                        " deals 3 damage to each nonwhite creature."):
                null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent permanent=(MagicPermanent)data[1];
				final Collection<MagicTarget> targets=
                    game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_NONWHITE_CREATURE);
				for (final MagicTarget target : targets) {
					final MagicDamage damage=new MagicDamage(permanent,target,3,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };
}
