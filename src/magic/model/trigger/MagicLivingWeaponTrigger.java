package magic.model.trigger;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPayedCost;
import magic.model.action.MagicAttachAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;

/**
 * Trigger that occurs when a card with the living weapon mechanic comes into play
 */
public class MagicLivingWeaponTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicWhenComesIntoPlayTrigger INSTANCE = new MagicLivingWeaponTrigger();

    private MagicLivingWeaponTrigger() {}

    public static MagicWhenComesIntoPlayTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
        return new MagicEvent(
            permanent,
            this,
            "PN puts a 0/0 black Germ creature token onto the battlefield, then attaches SN to it."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {

        //create the token
        final MagicPlayTokenAction play_token=new MagicPlayTokenAction(
            event.getPlayer(),
            TokenCardDefinitions.get("Germ")
        );
        game.doAction(play_token);

        //attach the equipment to the token
        final MagicAttachAction attach_equip = new MagicAttachAction(
            event.getPermanent(),
            play_token.getPermanent()
        );
        game.doAction(attach_equip);
    }
}
