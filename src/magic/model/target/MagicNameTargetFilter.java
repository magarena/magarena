package magic.model.target;

import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicNameTargetFilter extends MagicPermanentFilterImpl {

    private final String name;
    private final MagicTargetFilter<MagicPermanent> targetFilter;
    
    public MagicNameTargetFilter(final String aName) {
        this(MagicTargetFilterFactory.ANY, aName);
    }

    public MagicNameTargetFilter(final MagicTargetFilter<MagicPermanent> aTargetFilter, final String aName) {
        name = aName;
        targetFilter = aTargetFilter;
    }

    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return name.equals(target.getName()) && name.length() > 0  && targetFilter.accept(source, player, target);
    }
}
