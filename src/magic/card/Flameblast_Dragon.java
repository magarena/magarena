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

public class Flameblast_Dragon {

    public static final MagicTrigger V7293 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Flameblast Dragon") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {X}{R}.",new MagicPayManaCostChoice(MagicManaCost.X_RED),MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER), new MagicDamageTargetPicker(player.getMaximumX(game,MagicManaCost.X_RED)),
					new Object[]{permanent},this,"You may pay$ {X}{R}$. If you do, Flameblast Dragon deals X damage to target creature or player$.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicTarget target=event.getTarget(game,choiceResults,2);
				if (target!=null) {
					final MagicPayManaCostResult payedManaCost=(MagicPayManaCostResult)choiceResults[1];
					final MagicDamage damage=new MagicDamage((MagicPermanent)data[0],target,payedManaCost.getX(),false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };

}
