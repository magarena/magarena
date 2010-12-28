package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicAddEventAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class MagicDevourTrigger extends MagicTrigger {

	private final String name;
	private final int amount;
	
	public MagicDevourTrigger(final String name,final int amount) {
		
		super(MagicTriggerType.WhenComesIntoPlay,name);
		this.name=name;
		this.amount=amount;
	}
	
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
		final MagicPlayer player=permanent.getController();
		if (player.getNrOfPermanentsWithType(MagicType.Creature)>1) {
			final MagicTargetFilter targetFilter=new MagicTargetFilter.MagicOtherPermanentTargetFilter(
					MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,permanent);
			final MagicTargetChoice targetChoice=new MagicTargetChoice(
					targetFilter,false,MagicTargetHint.None,"a creature other than "+name+" to sacrifice");
			final MagicChoice devourChoice=new MagicMayChoice("You may sacrifice a creature to "+name+".",targetChoice);
			return new MagicEvent(permanent,player,devourChoice,MagicSacrificeTargetPicker.getInstance(),new Object[]{permanent},this,
				"You may$ sacrifice a creature$ to "+name+".");
		}		
		return null;
	}

	@Override
	public boolean usesStack() {

		return false;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

		if (MagicMayChoice.isYesChoice(choiceResults[0])) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,1);
			if (creature!=null) {
				final MagicPermanent permanent=(MagicPermanent)data[0];
				game.doAction(new MagicSacrificeAction(creature));
				game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount,true));
				final MagicEvent newEvent=executeTrigger(game,permanent,data);
				if (newEvent!=null) {
					game.doAction(new MagicAddEventAction(newEvent));
				}
			}
		}
	}
}