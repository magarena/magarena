package magic.model.target;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public class MagicDefaultPermanentPicker extends MagicTargetPicker<MagicPermanent> {

    private static final MagicDefaultPermanentPicker INSTANCE = new MagicDefaultPermanentPicker();

    private MagicDefaultPermanentPicker() {}

    public static MagicDefaultPermanentPicker create() {
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
