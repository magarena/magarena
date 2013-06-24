package magic.model.phase;

import magic.model.MagicGame;

public interface MagicGameplay {

    MagicPhase getStartPhase(final MagicGame game);

    MagicPhase getNextPhase(final MagicGame game);
}
