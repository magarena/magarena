package magic.exception;

import magic.model.MagicGame;

@SuppressWarnings("serial")
public class GameException extends RuntimeException {

    final MagicGame game;

    public GameException(final Throwable cause, final MagicGame aGame) {
        super(cause);
        game = aGame;
    }

    public MagicGame getGame() {
        return game;
    }
}
