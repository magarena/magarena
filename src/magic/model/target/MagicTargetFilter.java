package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

import java.util.List;

public interface MagicTargetFilter<T extends MagicTarget> {

    boolean acceptType(final MagicTargetType targetType);

    boolean accept(final MagicGame game,final MagicPlayer player,final T target);

    List<T> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint);
    
    List<T> filter(final MagicGame game);
    
    List<T> filter(final MagicPlayer player);
}
