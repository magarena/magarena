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
public class MagicLivingWeaponTrigger {
    private static final MagicWhenComesIntoPlayTrigger INSTANCE = new MagicWhenComesIntoPlayTrigger() {
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
		return new MagicEvent(
            permanent,
            player,
            new Object[]{permanent,player},
            this,
			player + " puts a 0/0 black Germ creature token onto the battlefield, then attaches this to it.");
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
                TokenCardDefinitions.getInstance().getTokenDefinition("Germ"));
		game.doAction(play_token);

        //attach the equipment to the token
        final MagicAttachEquipmentAction attach_equip = new MagicAttachEquipmentAction(
                (MagicPermanent)data[0],
                play_token.getPermanent());
        game.doAction(attach_equip);
	}
    };
    
    public static MagicWhenComesIntoPlayTrigger getInstance() {
        return INSTANCE;
    }
}
