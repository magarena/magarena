package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

import java.util.Collection;

public class MagicDefaultPermanentTargetPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicDefaultPermanentTargetPicker INSTANCE = new MagicDefaultPermanentTargetPicker();

    private MagicDefaultPermanentTargetPicker() {}

    public static MagicDefaultPermanentTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return 0;
    }

    @Override
    public Collection<MagicPermanent> pickTargets(final MagicGame game, final MagicPlayer player, final Collection<MagicPermanent> options) {
        return options;
    }
}
