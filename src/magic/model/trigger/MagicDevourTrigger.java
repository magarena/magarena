package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicAddEventAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

public class MagicDevourTrigger extends MagicWhenComesIntoPlayTrigger {

	private final int amount;
	
	public MagicDevourTrigger(final int amount) {
		this.amount=amount;
	}
	
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
        final String name = getCardDefinition().getFullName();
        final MagicTargetFilter targetFilter=new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,permanent);
        final MagicTargetChoice targetChoice=new MagicTargetChoice(
                targetFilter,false,MagicTargetHint.None,"a creature other than "+name+" to sacrifice");
        final MagicChoice devourChoice=new MagicMayChoice("You may sacrifice a creature to "+name+".",targetChoice);
		return (player.getNrOfPermanentsWithType(MagicType.Creature)>1) ?
            new MagicEvent(
                    permanent,
                    player,
                    devourChoice,
                    MagicSacrificeTargetPicker.getInstance(),
                    new Object[]{permanent},
                    this,
                    "You may$ sacrifice a creature$ to "+name+"."):
            MagicEvent.NONE;
	}

	@Override
	public boolean usesStack() {
		return false;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
		if (MagicMayChoice.isYesChoice(choiceResults[0])) {
            event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPermanent permanent=(MagicPermanent)data[0];
                    game.doAction(new MagicSacrificeAction(creature));
                    game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount,true));
                    final MagicEvent newEvent=executeTrigger(game,permanent,permanent.getController());
                    if (newEvent!=MagicEvent.NONE) {
                        game.doAction(new MagicAddEventAction(newEvent));
                    }
                }
			});
		}
	}
}
