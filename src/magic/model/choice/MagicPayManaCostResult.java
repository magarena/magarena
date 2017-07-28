package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicMappable;
import magic.model.MagicCopyable;

public interface MagicPayManaCostResult extends MagicMappable<MagicPayManaCostResult> {

    int getX();

    int getConverted();

    void doAction(final MagicGame game,final MagicPlayer player);
}
