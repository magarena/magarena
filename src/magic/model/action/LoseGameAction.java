package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

public class LoseGameAction extends MagicAction {

    public static final String LIFE_REASON   = " lost the game.";
    public static final String POISON_REASON = " lost the game because of poisoning.";
    public static final String DRAW_REASON   = " lost the game because of an attempt to draw from an empty library.";
    public static final String EFFECT_REASON = " lost the game because of an effect from a spell or ability.";
    public static final String ILLEGAL_REASON = " lost the game because of an illegal action.";

    private final String reason;
    private MagicPlayer player;
    private MagicPlayer oldLosingPlayer = MagicPlayer.NONE;

    public LoseGameAction(final MagicPlayer aPlayer,final String aReason) {
        player = aPlayer;
        reason = aReason;
    }

    public LoseGameAction(final MagicPlayer aPlayer) {
        this(aPlayer, LIFE_REASON);
    }

    public MagicPlayer getPlayer() {
        return player;
    }

    public String getReason() { return reason; }

    public void setPlayer(final MagicPlayer aPlayer) {
        player = aPlayer;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldLosingPlayer=game.getLosingPlayer();
        if (!oldLosingPlayer.isValid()) {
            if (reason != ILLEGAL_REASON) {
                game.executeTrigger(MagicTriggerType.IfPlayerWouldLose, this);
            }
            if (player.isValid()) {
                setScore(player,ArtificialScoringSystem.getLoseGameScore(game));
                game.setLosingPlayer(player);
                game.logMessage(player,"{L} " + player + reason);
            }
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.setLosingPlayer(oldLosingPlayer);
    }
}
