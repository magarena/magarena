package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicTrampleTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicTrampleTargetPicker INSTANCE=new MagicTrampleTargetPicker();

    private MagicTrampleTargetPicker() {}

    public static MagicTrampleTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        if (!permanent.hasAbility(MagicAbility.Trample)&&
            !permanent.hasAbility(MagicAbility.Defender)&&
            !permanent.hasAbility(MagicAbility.CannotAttackOrBlock)) {
            if (permanent.isBlocked()) {
                int toughness = 0; 
                for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                    toughness += blocker.getToughness();
                }
                if (toughness < permanent.getPower()) {
                    return 10+permanent.getPower();
                }
            }
            return 1+permanent.getPower();
        }
        return 0;
    }
}
