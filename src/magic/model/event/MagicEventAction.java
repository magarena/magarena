package magic.model.event;

import magic.model.MagicGame;

public interface MagicEventAction {
	void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults);
}
