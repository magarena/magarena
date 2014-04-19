package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class MagicEquipTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private final Set<MagicAbility> givenAbilityFlags = MagicAbility.noneOf();
    private final MagicPowerToughness givenPT = new MagicPowerToughness(0,0);

    public MagicEquipTargetPicker(final MagicPermanent equipment) {
        // determine given ability and given pt of equipment from list of static abilities
        for (final MagicStatic mstatic : equipment.getStatics()) {
            mstatic.modAbilityFlags(equipment, MagicPermanent.NONE, givenAbilityFlags);
            mstatic.modPowerToughness(equipment, MagicPermanent.NONE, givenPT);
        }
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        // penalty when already equipped
        int penalty = permanent.isEquipped() ? 3 : 0;

        // penalty when there is an overlap between abilities
        givenAbilityFlags.retainAll(permanent.getAbilityFlags());
        if (!givenAbilityFlags.isEmpty()) {
            penalty+=6;
        }

        final MagicPowerToughness pt = permanent.getPowerToughness();
        final boolean defensive = givenPT.toughness() > givenPT.power();

        // Defensive
        if (defensive) {
            return 20-pt.toughness()-penalty;
        } else {
        // Offensive
            return 1+pt.power()*2-pt.toughness()-penalty;
        }
    }
}
