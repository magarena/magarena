package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

import java.util.Collection;

public class MagicDefaultTargetPicker extends MagicTargetPicker<MagicTarget> {

    private static final MagicDefaultTargetPicker INSTANCE = new MagicDefaultTargetPicker();

    private MagicDefaultTargetPicker() {}

    public static MagicDefaultTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
        return 0;
    }

    @Override
    public Collection<MagicTarget> pickTargets(final MagicGame game, final MagicEvent event, final Collection<MagicTarget> options) {
        return options;
    }
}
