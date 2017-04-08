package magic.model.trigger;

import magic.data.CardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.AttachAction;
import magic.model.action.PlayTokenAction;
import magic.model.event.MagicEvent;

/**
 * Trigger that occurs when a card with the living weapon mechanic comes into play
 */
public class LivingWeaponTrigger extends EntersBattlefieldTrigger {

    private static final EntersBattlefieldTrigger INSTANCE = new LivingWeaponTrigger();

    private LivingWeaponTrigger() {}

    public static EntersBattlefieldTrigger create() {
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
        final MagicPermanent sn = event.getPermanent();
        game.doAction(new PlayTokenAction(
            event.getPlayer(),
            CardDefinitions.getToken("0/0 black Germ creature token"),
            (final MagicPermanent perm) -> {
                final MagicGame G = perm.getGame();
                G.doAction(new AttachAction(
                    sn.map(G),
                    perm
                ));
            }
        ));
    }
}
