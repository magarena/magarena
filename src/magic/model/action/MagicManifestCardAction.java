
package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPayedCost;
import magic.model.event.MagicPlayCardEvent;
import magic.model.stack.MagicCardOnStack;

public class MagicManifestCardAction extends MagicPlayCardAction {

    public MagicManifestCardAction(final MagicCard card, final MagicPlayer player) {
        super(card, player, MagicPlayMod.MANIFEST);
    }
    
    @Override
    public void doAction(final MagicGame game) {
        final MagicCardOnStack cardOnStack = new MagicCardOnStack(
            card,
            card,
            controller,
            MagicPlayCardEvent.create(),
            MagicPayedCost.NOT_SPELL,
            modifications
        );
        game.addEvent(cardOnStack.getEvent());
    }
}
