package magic.model.target;

import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.MagicType;

import java.util.EnumSet;
import java.util.Set;

public class MagicPlaneswalkerCopiesFilter extends MagicPermanentFilterImpl {

    private final Set<MagicSubType> pwTypes = EnumSet.noneOf(MagicSubType.class);

    public MagicPlaneswalkerCopiesFilter(final MagicPermanent permanent) {
        for (final MagicSubType st : MagicSubType.ALL_PLANESWALKERS) {
            if (permanent.hasSubType(st)) {
                pwTypes.add(st);
            }
        }
    }

    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        if (target.hasType(MagicType.Planeswalker) == false) {
            return false;
        }
        if (target.isController(player) == false) {
            return false;
        }
        for (final MagicSubType st : pwTypes) {
            if (target.hasSubType(st)) {
                return true;
            }
        }
        return false;
    }
}
