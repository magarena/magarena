package magic.model.choice;

import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;

// need MagicCopyable as it can be stored on the stack
public interface MagicPayManaCostResult extends MagicCopyable, MagicMappable<MagicPayManaCostResult> {

    int getX();

    int getConverted();

    void doAction(final MagicGame game,final MagicPlayer player);
}
