package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.action.ReturnExiledAction;
import magic.model.event.MagicDrawEvent;
import magic.model.trigger.MagicTriggerType;
import magic.ui.MagicSound;

public class MagicEndOfTurnPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicEndOfTurnPhase();

    private MagicEndOfTurnPhase() {
        super(MagicPhaseType.EndOfTurn);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        // Exiled until end of turn.
        game.doAction(new ReturnExiledAction());

        // End of turn triggers.
        if (game.getTurnPlayer().isMonarch()) {
            game.addEvent(new MagicDrawEvent(MagicSource.NONE,game.getTurnPlayer(),1));
        }
        game.executeTrigger(MagicTriggerType.AtEndOfTurn,game.getTurnPlayer());
        game.setStep(MagicStep.ActivePlayer);
    }

    @Override
    protected void executeEndOfPhase(final MagicGame game) {
        game.playSound(MagicSound.NEW_TURN);
    }
}
