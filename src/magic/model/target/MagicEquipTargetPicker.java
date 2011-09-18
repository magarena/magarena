package magic.model.target;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;

public class MagicEquipTargetPicker extends MagicTargetPicker<MagicPermanent> {
	
	private final MagicCardDefinition cdef;
	
	public MagicEquipTargetPicker(final MagicCardDefinition cardDefinition) {
        cdef = cardDefinition; 
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		// Penalty when allready equipped.
		int penalty=permanent.isEquipped()?3:0;

        // Penalty when there is an overlap between abilities.
	    final long abilityFlags = cdef.getAbilityFlags();
		if ((permanent.getAllAbilityFlags(game)&abilityFlags)!=0) {
			penalty+=6;
		}
		
        final MagicPowerToughness pt=permanent.getPowerToughness(game);
		final boolean defensive = cdef.getToughness(game) > cdef.getPower(game);
        
        // Defensive
		if (defensive) {
			return 20-pt.toughness()-penalty;
		} else {
		// Offensive
		    return 1+pt.power()*2-pt.toughness()-penalty;
        }
	}
}
