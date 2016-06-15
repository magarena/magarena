package magic.model.action;

import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicGame;
import magic.model.trigger.MagicTriggerType;

public class SacrificeAction extends RemoveFromPlayAction {
    public SacrificeAction(final MagicPermanent permanent) {
        super(permanent,MagicLocationType.Graveyard);
    }

    @Override
    public void doAction(final MagicGame game) {
        game.executeTrigger(MagicTriggerType.WhenSacrifice, this);
        super.doAction(game);
    }
}
