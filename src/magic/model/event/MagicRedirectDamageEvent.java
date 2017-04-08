package magic.model.event;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.DealDamageAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;

public class MagicRedirectDamageEvent extends MagicEvent {

    public MagicRedirectDamageEvent(final MagicDamage damage) {
        super(
            damage.getSource(),
            new MagicMayChoice(
                "Redirect " + damage.getAmount() + " damage to a planeswalker?",
                MagicTargetChoice.PLANESWALKER_YOUR_OPPONENT_CONTROLS
            ),
            damage.getTarget(),
            EventAction(damage.getAmount(), damage.isCombat()),
            "PN may$ redirect " + damage.getAmount() +
            " damage to a planeswalker$ your opponent controls."
        );
    }

    private static final MagicEventAction EventAction(final int amount, final boolean isCombat) {
        return (final MagicGame game, final MagicEvent event) -> {
            if (event.isYes()) {
                event.processTargetPermanent(game, (final MagicPermanent planeswalker) -> {
                    final MagicDamage damage = isCombat ?
                        MagicDamage.Combat(event.getSource(), planeswalker, amount) :
                        new MagicDamage(event.getSource(), planeswalker, amount);
                    game.doAction(new DealDamageAction(damage));
                });
            } else {
                final MagicDamage damage = isCombat ?
                    MagicDamage.Combat(event.getSource(), event.getRefPlayer(), amount) :
                    new MagicDamage(event.getSource(), event.getRefPlayer(), amount);
                game.doAction(DealDamageAction.NoRedirect(damage));
            }
        };
    }
}
