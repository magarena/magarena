package magic.model.target;

import java.util.List;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

public interface MagicTargetFilter<T extends MagicTarget> {

    default boolean isStatic() {
        return false;
    }

    boolean acceptType(final MagicTargetType targetType);

    boolean accept(final MagicSource source,final MagicPlayer player,final T target);

    default List<T> filter(final MagicSource source, final MagicPlayer player) {
        return filter(source, player, MagicTargetHint.None);
    }

    List<T> filter(final MagicSource source, final MagicPlayer player, final MagicTargetHint targetHint);

    List<T> filter(final MagicSource source);

    List<T> filter(final MagicPlayer player);

    List<T> filter(final MagicEvent event);
}
