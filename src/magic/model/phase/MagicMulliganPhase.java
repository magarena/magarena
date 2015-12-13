package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicMulliganEvent;
import magic.model.event.MagicScryEvent;

public class MagicMulliganPhase extends MagicPhase {

    private static final MagicPhase INSTANCE = new MagicMulliganPhase();

    private MagicMulliganPhase() {
        super(MagicPhaseType.Mulligan);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        for (final MagicPlayer player : game.getPlayers()) {
            game.addEvent(new MagicMulliganEvent(player));
        }
        game.setStep(MagicStep.NextPhase);
    }

    @Override
    void executeEndOfPhase(final MagicGame game) {
        for (final MagicPlayer player : game.getPlayers()) {
            if (player.getHandSize() < player.getStartingHandSize()) {
                game.addEvent(new MagicScryEvent(MagicSource.NONE, player));
            }
        }
    }
}
