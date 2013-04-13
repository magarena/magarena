package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicWhenSpellIsCastTrigger;

public class Artisan_of_Kozilek {
    public static final MagicWhenSpellIsCastTrigger T = new MagicWhenSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack spell) {
            return new MagicEvent(
                spell,
                new MagicMayChoice(
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD
                ),
                new MagicGraveyardTargetPicker(true),
                this,
                "PN may$ return target creature card$ from " +
                "his or her graveyard to the battlefield."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicReanimateAction(
                            event.getPlayer(),
                            card,
                            MagicPlayCardAction.NONE
                        ));
                    }
                });
            }
        }
    };
}
