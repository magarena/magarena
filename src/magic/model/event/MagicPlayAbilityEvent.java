package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPlayAbilityAction;

public class MagicPlayAbilityEvent extends MagicEvent {
	public MagicPlayAbilityEvent(final MagicPermanent source) {
		super(
            source,
            source.getController(),
            MagicEvent.NO_DATA,
            new MagicEventAction() {
            @Override
            public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
                game.doAction(new MagicPlayAbilityAction(source.map(game)));
            }},
            "");
	}
}
