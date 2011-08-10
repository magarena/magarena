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

public class Rise_of_the_Hobgoblins {

	public static final MagicPermanentActivation V2460 =new MagicPermanentActivation(			"Rise of the Hobgoblins",
            new MagicCondition[]{MagicManaCost.RED_OR_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Block,true),
            "First strike") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.RED_OR_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Red creatures and white creatures you control gain first strike until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				final MagicPermanent creature=(MagicPermanent)target;
				final int colorFlags=creature.getColorFlags();
				if (MagicColor.Red.hasColor(colorFlags)||MagicColor.White.hasColor(colorFlags)) {
					game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
				}
			}
		}
	};

    public static final MagicTrigger V10273 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Rise of the Hobgoblins") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,
	new MagicMayChoice("You may pay {X}.",new MagicPayManaCostChoice(MagicManaCost.X)),
    new Object[]{player},this,
				"You may pay$ {X}$. If you do, put X 1/1 red and white Goblin Soldier creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final MagicPayManaCostResult payedManaCost=(MagicPayManaCostResult)choiceResults[1];
				
				for (int count=payedManaCost.getX();count>0;count--) {
					
					game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.GOBLIN_SOLDIER_TOKEN_CARD));
				}
			}
		}
    };
     
}
