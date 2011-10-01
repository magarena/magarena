package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicTargetFilter;

public class Wreath_of_Geists {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
	    MagicTargetFilter.TARGET_CREATURE) {
        @Override
        public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
        	final int amount = game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD).size();
            pt.add(amount,amount);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {	
        	return target == source.getEnchantedCreature();
        }
    };
    
    public static final MagicSpellCardEvent E = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
			MagicPumpTargetPicker.getInstance());
}
