package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicIndestructibleTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicIndestructibleTargetPicker INSTANCE=new MagicIndestructibleTargetPicker();

    private MagicIndestructibleTargetPicker() {}

    public static MagicIndestructibleTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (permanent.hasAbility(MagicAbility.Indestructible)) {
            return 0;
        }
        return 100+permanent.getPower()*2+permanent.getDamage()-permanent.getToughness();
    }
}
