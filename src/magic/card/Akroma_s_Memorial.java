package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.MagicAbility;

public class Akroma_s_Memorial {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            return flags | 
                   MagicAbility.Flying.getMask() |
		           MagicAbility.FirstStrike.getMask() |
		           MagicAbility.Vigilance.getMask() |
		           MagicAbility.Trample.getMask() |
		           MagicAbility.Haste.getMask() |
		           MagicAbility.ProtectionFromBlack.getMask() |
		           MagicAbility.ProtectionFromRed.getMask();
        }
    };
}
