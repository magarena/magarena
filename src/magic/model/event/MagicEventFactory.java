package magic.model.event;

import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.MagicCopyable;

public interface MagicEventFactory {

    boolean accept(final MagicSource source, final MagicEvent ev);
    MagicEvent getEvent(final MagicSource source, final MagicPlayer player, final MagicCopyable ref);

    default MagicEvent getTriggerEvent(final MagicSource source, final MagicPlayer player, final MagicCopyable ref) {
        final MagicEvent ev = getEvent(source, player, ref);
        return accept(source, ev) ? ev : MagicEvent.NONE;
    }
    default MagicEvent getTriggerEvent(final MagicSource source, final MagicCopyable ref) {
        final MagicEvent ev = getEvent(source, ref);
        return accept(source, ev) ? ev : MagicEvent.NONE;
    }
    default MagicEvent getTriggerEvent(final MagicSource source) {
        final MagicEvent ev = getEvent(source);
        return accept(source, ev) ? ev : MagicEvent.NONE;
    }
    default MagicEvent getEvent(final MagicSource source, final MagicCopyable ref) {
        return getEvent(source, source.getController(), ref);
    }
    default MagicEvent getEvent(final MagicSource source) {
        return getEvent(source, source.getController(), MagicEvent.NO_REF);
    }
    default MagicEvent getEvent(final MagicEvent event) {
        return getEvent(event.getSource(), event.getPlayer(), MagicEvent.NO_REF);
    }

    public static MagicEventFactory create(final String text) {
        if (text.contains("that player controls")) {
            final MagicSourceEvent youVersion = MagicRuleEventAction.create(text.replace("that player controls", "you control"));
            final MagicSourceEvent oppVersion = MagicRuleEventAction.create(text.replace("that player controls", "an opponent controls"));
            return new MagicPlayerTriggerEvent(youVersion, oppVersion);
        } else if (text.contains("that player's graveyard")) {
            final MagicSourceEvent youVersion = MagicRuleEventAction.create(text.replace("that player's graveyard", "your graveyard"));
            final MagicSourceEvent oppVersion = MagicRuleEventAction.create(text.replace("that player's graveyard", "an opponent's graveyard"));
            return new MagicPlayerTriggerEvent(youVersion, oppVersion);
        } else {
            return MagicRuleEventAction.create(text);
        }
    }
}
