package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.stack.MagicCardOnStack;

/** Creatures or spells from both players. */
public class MagicBounceTargetPicker extends MagicTargetPicker<MagicTarget> {

    private static final MagicBounceTargetPicker INSTANCE = new MagicBounceTargetPicker();

    private MagicBounceTargetPicker() {}

    public static MagicBounceTargetPicker create() {
        return INSTANCE;
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
        if (target.isPermanent()) {
            return MagicBouncePermanentPicker.create().getTargetScore(game, player, (MagicPermanent)target);
        } else {
            //target is MagicCardOnStack
            final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
            final int converted=1+cardOnStack.getCardDefinition().getConvertedCost();
            return cardOnStack.getController()==player?-converted:converted;
        }
    }
}
