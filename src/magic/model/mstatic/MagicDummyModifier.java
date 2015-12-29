package magic.model.mstatic;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;

import java.util.Set;

public class MagicDummyModifier implements MagicModifier {

    @Override
    public MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer controller) {
        return controller;
    }

    @Override
    public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
        //leave power and toughness unchanged
    }

    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        //leave abilities unchanged
    }

    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        //leave subtype unchanged
    }

    @Override
    public int getTypeFlags(final MagicPermanent permanent, final int flags) {
        return flags;
    }

    @Override
    public int getColorFlags(final MagicPermanent permanent, final int flags) {
        return flags;
    }

    @Override
    public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
        //leave player unchanged
    }

    @Override
    public void modGame(final MagicPermanent source, final MagicGame game) {
        //leave game unchanged
    }
    
    @Override
    public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
        return cost;
    }
    
    @Override
    public MagicManaCost increaseCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
        return cost;
    }
}
