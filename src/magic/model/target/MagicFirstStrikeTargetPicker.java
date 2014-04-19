package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicFirstStrikeTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicFirstStrikeTargetPicker INSTANCE=new MagicFirstStrikeTargetPicker();

    private MagicFirstStrikeTargetPicker() {}

    public static MagicFirstStrikeTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (permanent.getController()!=player) {
            return -50-permanent.getPower();
        }
        if (permanent.hasAbility(MagicAbility.FirstStrike) ||
            permanent.hasAbility(MagicAbility.DoubleStrike)) {
            return 0;
        }
        final int power=permanent.getPower();
        if (permanent.isBlocked()||permanent.isBlocking()) {
            return power+permanent.getBlockingCreatures().size()+100;
        }
        if (permanent.canTap()) {
            return power+50;
        }
        return power+1;
    }
}
