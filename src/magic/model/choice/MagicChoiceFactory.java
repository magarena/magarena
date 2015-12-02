package magic.model.choice;

import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.MagicCopyable;

public interface MagicChoiceFactory {
    MagicChoice build(final MagicSource source, final MagicPlayer player, final MagicCopyable ref);
}
