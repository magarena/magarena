package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicRegenerateTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicRegenerateTargetPicker INSTANCE = new MagicRegenerateTargetPicker();

    private MagicRegenerateTargetPicker() {}

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (permanent.canRegenerate()) {
            if (permanent.hasAbility(MagicAbility.Persist) ||
                permanent.hasAbility(MagicAbility.Indestructible)) {
                return permanent.getDamage()+1;
            }
            return permanent.getScore();
        }
        return 0;
    }

    public static MagicRegenerateTargetPicker create() {
        return INSTANCE;
    }
}
