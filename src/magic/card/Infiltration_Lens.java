package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Infiltration_Lens {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            if (creature == equippedCreature) {
                final int amount = equippedCreature.getBlockingCreatures().size();
                final MagicPlayer player = permanent.getController();
                return new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may draw two cards.",
                                MagicSimpleMayChoice.DRAW_CARDS,
                                2,
                                MagicSimpleMayChoice.DEFAULT_NONE),
                        new Object[]{permanent,player,amount},
                        this,
                        player + " may draw two cards.");
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = (MagicPermanent)data[0];
            final MagicPlayer player = (MagicPlayer)data[1];
            int amount = (Integer)data[2];
            if (MagicChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicDrawAction(player,2));
            }        
            amount--;
            if (amount > 0) {
                final MagicEvent triggerEvent = new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may draw two cards.",
                                MagicSimpleMayChoice.DRAW_CARDS,
                                2,
                                MagicSimpleMayChoice.DEFAULT_NONE),
                        new Object[]{permanent,player,amount},
                        DRAW,
                        player + " may draw two cards."
                    );
                    game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(triggerEvent)));
            }
        }
    };
    
    private static final MagicEventAction DRAW = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPermanent permanent = (MagicPermanent)data[0];
            final MagicPlayer player = (MagicPlayer)data[1];
            int amount = (Integer)data[2];
            if (MagicChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicDrawAction(player,2));
            }        
            amount--;
            if (amount > 0) {
                final MagicEvent triggerEvent = new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may draw two cards.",
                                MagicSimpleMayChoice.DRAW_CARDS,
                                2,
                                MagicSimpleMayChoice.DEFAULT_NONE),
                        new Object[]{permanent,player,amount},
                        DRAW,
                        player + " may draw two cards."
                    );
                    game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(triggerEvent)));
            }
        }
    };
}
