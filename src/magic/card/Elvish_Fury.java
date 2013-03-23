package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPumpTargetPicker;

public class Elvish_Fury {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicBuybackChoice(
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicManaCost.create("{4}")
                ),
                MagicPumpTargetPicker.create(),
                this,
                "Target creature$ gets +2/+2 until end of turn. " + 
                "If the buyback cost was payed$, return SN to its owner's hand as it resolves."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,2,2));
                    if (event.isBuyback()) {
                        game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
                    } 
                }
            });
        }
    };
}
