package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;

public class MagicLegendaryCopiesFilter extends MagicPermanentFilterImpl {

    private final String name;

    public MagicLegendaryCopiesFilter(final String name) {
        this.name=name;
    }

    @Override
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return name.equals(target.getName()) &&
               target.hasType(MagicType.Legendary) &&
               target.isController(player);
    }
}
