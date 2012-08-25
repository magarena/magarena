package magic.model.target;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicStatic;

public class MagicEquipTargetPicker extends MagicTargetPicker<MagicPermanent> {
    
    private final long givenAbilityFlags;
    private final MagicPowerToughness givenPT;
    
    public MagicEquipTargetPicker(final MagicPermanent equipment) {
        // determine given ability and given pt of equipment from list of static abilities
        long ability = 0;
        MagicPowerToughness pt = new MagicPowerToughness(0,0);

        for (final MagicStatic mstatic : equipment.getCardDefinition().getStatics()) {
            ability = mstatic.getAbilityFlags(equipment, MagicPermanent.NONE, ability);
            mstatic.modPowerToughness(equipment, MagicPermanent.NONE, pt);
        }

        givenAbilityFlags = ability;
        givenPT = pt;
    }
    
    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
        // penalty when already equipped
        int penalty = permanent.isEquipped() ? 3 : 0;
        
        // penalty when there is an overlap between abilities.
        if ((permanent.getAllAbilityFlags() & givenAbilityFlags) != 0) {
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
