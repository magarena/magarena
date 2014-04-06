package magic.model.target;

import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public interface MagicTargetFilter<T extends MagicTarget> {

    boolean acceptType(final MagicTargetType targetType);

    boolean accept(final MagicGame game,final MagicPlayer player,final T target);

    List<T> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint);
}
