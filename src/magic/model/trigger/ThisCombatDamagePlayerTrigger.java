package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicMessage;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.event.MagicEvent;
import magic.model.action.ShiftCardAction;

public abstract class ThisCombatDamagePlayerTrigger extends DamageIsDealtTrigger {
    public ThisCombatDamagePlayerTrigger() {}

    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return super.accept(permanent, damage) &&
               damage.isSource(permanent) &&
               damage.isCombat() &&
               damage.isTargetPlayer();
    }

    public static final ThisCombatDamagePlayerTrigger Ingest = new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTarget(),
                this,
                "RN exiles the top card of his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getRefPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.Exile
                ));
                game.logAppendMessage(event.getPlayer(), MagicMessage.format("%s is exiled.", card));
            }
        }
    };
}
