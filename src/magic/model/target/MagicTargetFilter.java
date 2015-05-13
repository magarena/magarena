package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

import java.util.List;

public interface MagicTargetFilter<T extends MagicTarget> {

    boolean acceptType(final MagicTargetType targetType);

    boolean accept(final MagicSource source,final MagicPlayer player,final T target);
    
    List<T> filter(final MagicSource source, final MagicPlayer player, final MagicTargetHint targetHint);
    
    List<T> filter(final MagicSource source);

    List<T> filter(final MagicPlayer player);
    
    List<T> filter(final MagicEvent event);
}
