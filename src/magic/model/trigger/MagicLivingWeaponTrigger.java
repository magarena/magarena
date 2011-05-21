package magic.model.trigger;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;

/**
 * Trigger that occurs when a card with the living weapon mechanic comes into play
 */
public class MagicLivingWeaponTrigger extends MagicTrigger {

	public MagicLivingWeaponTrigger(final String name) {
		super(MagicTriggerType.WhenComesIntoPlay,name);
	}
	
	@Override
	public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final Object data) {

		final MagicPlayer player=permanent.getController();
		return new MagicEvent(
            permanent,
            player,
            new Object[]{permanent,player},
            this,
			"You put a 0/0 black Germ creature token onto the battlefield, then attach this to it.");
	}
	
	@Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {

        //create the token
		final MagicPlayTokenAction play_token=new MagicPlayTokenAction(
                (MagicPlayer)data[1],
                TokenCardDefinitions.GERM_TOKEN_CARD);
		game.doAction(play_token);

        //attach the equipment to the token
        final MagicAttachEquipmentAction attach_equip = new MagicAttachEquipmentAction(
                (MagicPermanent)data[0],
                action.getPermanent());
        game.doAction(attach_equip)
	}
}
