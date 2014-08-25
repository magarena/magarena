package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDiscardCardAction;
import magic.model.trigger.MagicWhenCycleTrigger;

public class MagicDiscardSelfEvent extends MagicEvent {

    public MagicDiscardSelfEvent(final MagicCard card) {
        this(card, 0);
    }

    public static MagicDiscardSelfEvent Cycle(final MagicCard card) {
        return new MagicDiscardSelfEvent(card, 1);
    }
    
    private MagicDiscardSelfEvent(final MagicCard card, final int cycle) {
        super(
            card,
            cycle,
            EVENT_ACTION,
            "PN discards SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            game.doAction(new MagicDiscardCardAction(event.getPlayer(),card));
            if (event.getRefInt() == 1) {
                for (final MagicWhenCycleTrigger trigger : card.getCardDefinition().getCycleTriggers()) {
                    game.executeTrigger(
                        trigger,
                        MagicPermanent.NONE,
                        card,
                        card
                    );
                }
            }
        }
    };
}
