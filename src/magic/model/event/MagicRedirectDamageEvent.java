package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicDamage;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicDealDamageAction;
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
        return new MagicEventAction() {
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] choiceResults) {
                if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                    event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent planeswalker) {
                            final MagicDamage damage = new MagicDamage(event.getSource(), planeswalker, amount, isCombat);
                            game.doAction(new MagicDealDamageAction(damage));
                        }
                    });
                } else {
                    final MagicDamage damage = new MagicDamage(event.getSource(), event.getRefPlayer(), amount, isCombat);
                    game.doAction(MagicDealDamageAction.NoRedirect(damage));
                }
            }
        };
    }
}
