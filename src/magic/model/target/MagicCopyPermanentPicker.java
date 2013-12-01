package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicCopyPermanentPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicCopyPermanentPicker INSTANCE=new MagicCopyPermanentPicker();

    private MagicCopyPermanentPicker() {}

    public static MagicCopyPermanentPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        return permanent.getCardScore();
    }
}
