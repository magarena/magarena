package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicHasteTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicHasteTargetPicker INSTANCE=new MagicHasteTargetPicker();

    private MagicHasteTargetPicker() {}

    public static MagicHasteTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (permanent.isTapped()||permanent.canTap()) {
            return 0;
        }
        return 1+permanent.getPower()+permanent.getActivations().size();
    }
}
