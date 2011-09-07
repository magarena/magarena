package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

public class MagicTappedIntoPlayUnlessTrigger extends MagicWhenComesIntoPlayTrigger {

	private final MagicSubType subType1;
	private final MagicSubType subType2;
	
    public MagicTappedIntoPlayUnlessTrigger(final MagicSubType subType1,final MagicSubType subType2) {
		this.subType1=subType1;
		this.subType2=subType2;
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
		return (!player.controlsPermanentWithSubType(subType1)&&!player.controlsPermanentWithSubType(subType2)) ?
            new MagicEvent(
                permanent, 
                player, 
                MagicEvent.NO_DATA, 
                permanent+" enters the battlefield tapped.",
                new MagicEventAction() {
                @Override
                public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] data,
                    final Object[] choices) {
                    game.doAction(new MagicTapAction(permanent.map(game),false));
                }}):
            MagicEvent.NONE;
	}
	
	@Override
	public boolean usesStack() {
		return false;
	}
}
