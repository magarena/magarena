package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.GainAbilityAction;
import magic.model.choice.MagicColorChoice;

public class MagicGainProtectionFromEvent extends MagicEvent {

    public MagicGainProtectionFromEvent(final MagicSource source, final MagicPlayer player, final MagicPermanent perm) {
        super(
            source,
            player,
            MagicColorChoice.ALL_INSTANCE,
            perm,
            EVENT_ACTION,
            "Chosen color$."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        game.doAction(new GainAbilityAction(
            event.getRefPermanent(),
            event.getChosenColor().getProtectionAbility()
        ));
    };
}
