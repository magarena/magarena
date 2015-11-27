package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.ChangeLifeAction;
import magic.model.action.TapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;

public class RavnicaLandTrigger extends EntersBattlefieldTrigger {

    private static final RavnicaLandTrigger INSTANCE = new RavnicaLandTrigger();

    private RavnicaLandTrigger() {
        super(MagicTrigger.REPLACEMENT);
    }

    public static RavnicaLandTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
        if (permanent.getController().getLife() < 2) {
            game.doAction(TapAction.Enters(permanent));
            return MagicEvent.NONE;
        } else {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may$ pay 2 life. If PN doesn't, SN enters the battlefield tapped."
            );
        }
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
        } else {
            game.doAction(TapAction.Enters(event.getPermanent()));
        }
    }
}
