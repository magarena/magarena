package magic.model.choice;

import magic.model.MagicCopyable;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public interface MagicChoiceFactory {
    MagicChoice build(final MagicSource source, final MagicPlayer player, final MagicCopyable ref);
}
