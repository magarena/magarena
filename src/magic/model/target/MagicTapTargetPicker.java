package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public abstract class MagicTapTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final int score(final MagicPermanent permanent) {
        return 1 + permanent.getPower() + permanent.getActivations().size();
    }

    public static MagicTapTargetPicker Tap = new MagicTapTargetPicker() {
        @Override
        protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            if (permanent.isTapped()) {
                return 0;
            } else {
                return permanent.isController(player) ? -score(permanent) : score(permanent);
            }
        }
    };

    public static MagicTapTargetPicker Untap = new MagicTapTargetPicker() {
        @Override
        protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            if (permanent.isUntapped()) {
                return 0;
            } else {
                return permanent.isOpponent(player) ? -score(permanent) : score(permanent);
            }
        }
    };

    public static MagicTapTargetPicker TapOrUntap = new MagicTapTargetPicker() {
        @Override
        protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return score(permanent);
        }
    };
}
