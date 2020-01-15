package magic.model.target;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

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
    public Collection<MagicPermanent> pickTargets(final MagicGame game, final MagicEvent event, final Collection<MagicPermanent> options) {
        return options;
    }
}
