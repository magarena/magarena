package magic.model.target;

import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;

public class MagicLegendaryCopiesFilter extends MagicPermanentFilterImpl {

    private final String name;

    public MagicLegendaryCopiesFilter(final String name) {
        this.name=name;
    }

    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isName(name) &&
               target.hasType(MagicType.Legendary) &&
               target.isController(player);
    }
}
