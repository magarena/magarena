package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicSacrificeTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicSacrificeTargetPicker INSTANCE=new MagicSacrificeTargetPicker();

    private MagicSacrificeTargetPicker() {}

    public static MagicSacrificeTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (!permanent.isOwner(player)) {
            return -permanent.getScore()+100;
        } else {
            return -permanent.getScore();
        }
    }
}
