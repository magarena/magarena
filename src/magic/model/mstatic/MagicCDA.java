package magic.model.mstatic;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;

import java.util.EnumSet;

/*
604.3a A static ability is a characteristic-defining ability if it meets
the following criteria: (1) It defines an object's colors, subtypes,
power, or toughness; (2) it is printed on the card it affects, it was
granted to the token it affects by the effect that created the token,
or it was acquired by the object it affects as the result of a copy
effect or text-changing effect; (3) it does not directly affect the
characteristics of any other objects; (4) it is not an ability that
an object grants to itself; and (5) it does not set the values of such
characteristics only if certain conditions are met.
*/

public abstract class MagicCDA {

	public int getColorFlags(final MagicGame game, final MagicPlayer player,final int flags) {
        return flags;
    }
	
    public EnumSet<MagicSubType> getSubTypeFlags(final MagicGame game, final MagicPlayer player,final EnumSet<MagicSubType> flags) {
        return flags;
    }
	
    public void getPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
    
    }
}
