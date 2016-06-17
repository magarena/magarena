package magic.model.phase;

import magic.model.MagicGame;
import magic.model.event.MagicDeclareBlockersEvent;

public class MagicDeclareBlockersPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicDeclareBlockersPhase();

    private MagicDeclareBlockersPhase() {
        super(MagicPhaseType.DeclareBlockers);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        game.addEvent(new MagicDeclareBlockersEvent(game.getDefendingPlayer()));
        game.setStep(MagicStep.ActivePlayer);
    }
}
