package magic.model.event;

import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.MagicCopyable;

public class MagicPlayerTriggerEvent implements MagicEventFactory {
    private final MagicSourceEvent you;
    private final MagicSourceEvent opp;

    public MagicPlayerTriggerEvent(final MagicSourceEvent aYou, final MagicSourceEvent aOpp) {
        you = aYou;
        opp = aOpp;
    }

    @Override
    public MagicEvent getEvent(final MagicSource source, final MagicPlayer player, final MagicCopyable ref) {
        final MagicPlayer p = (MagicPlayer)ref;
        if (source.isFriend(p)) {
            return you.getEvent(source, player, ref);
        } else {
            return opp.getEvent(source, player, ref);
        }
    }

    @Override
    public boolean accept(final MagicSource source, final MagicEvent ev) {
        final MagicPlayer p = ev.getRefPlayer();
        if (source.isFriend(p)) {
            return you.accept(source, ev);
        } else {
            return opp.accept(source, ev);
        }
    }
}
