package magic.model.event;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicTuple;
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
            new MagicTuple(damage.getAmount(), damage.isCombat() ? 1 : 0, damage.getTarget()),
            EventAction,
            "PN may$ redirect " + damage.getAmount() +
            " damage to a planeswalker$ your opponent controls."
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicTuple tup = event.getRefTuple();
        final int amount = tup.getInt(0);
        final boolean isCombat = tup.getInt(1) == 1;
        if (event.isYes()) {
            event.processTargetPermanent(game, (final MagicPermanent planeswalker) -> {
                final MagicDamage damage = isCombat ?
                    MagicDamage.Combat(event.getSource(), planeswalker, amount) :
                    new MagicDamage(event.getSource(), planeswalker, amount);
                game.doAction(new DealDamageAction(damage));
            });
        } else {
            final MagicDamage damage = isCombat ?
                MagicDamage.Combat(event.getSource(), tup.getPlayer(2), amount) :
                new MagicDamage(event.getSource(), tup.getPlayer(2), amount);
            game.doAction(DealDamageAction.NoRedirect(damage));
        }
    };
}
