package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicToughnessTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicToughnessTargetPicker INSTANCE = new MagicToughnessTargetPicker();

    private MagicToughnessTargetPicker() {}

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        return permanent.getToughness();
    }

    public static MagicToughnessTargetPicker create() {
        return INSTANCE;
    }
}
