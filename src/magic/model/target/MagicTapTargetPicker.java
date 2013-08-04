package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicTapTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private final boolean toTap;

    public static MagicTapTargetPicker Untap = new MagicTapTargetPicker(false);
    public static MagicTapTargetPicker Tap = new MagicTapTargetPicker(true);

    private MagicTapTargetPicker(final boolean aToTap) {
        toTap = aToTap;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        final boolean isTapped = permanent.isTapped();
        if (isTapped == toTap) {
            return 0;
        }
        final boolean isController = permanent.isController(player);
        return (isController == isTapped) ?
            1 + permanent.getPower()+permanent.getActivations().size() :
            0;
    }
    
    public static MagicTapTargetPicker TapOrUntap = new MagicTapTargetPicker(false) {
        @Override
        protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            return 1 + permanent.getPower()+permanent.getActivations().size();
        }
    };
}
