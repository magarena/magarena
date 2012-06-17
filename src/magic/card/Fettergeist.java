package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Fettergeist {
    private static final MagicEventAction EVENT = new MagicEventAction() {
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
            if (MagicMayChoice.isNoChoice(choiceResults[0])) {
                game.doAction(new MagicSacrificeAction((MagicPermanent)event.getSource()));
            }
        }
    };
    
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent},
                        this,
                        "Sacrifice " + permanent + " unless you pay " +
                        "{1} for each other creature you control.") :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final int x = event.getPlayer().controlsPermanent((MagicPermanent)event.getSource()) ? 1 : 0;
            final int amount = event.getPlayer().getNrOfPermanentsWithType(MagicType.Creature) - x;
            final MagicManaCost cost = MagicManaCost.createCost("{"+amount+"}");
            final MagicEvent triggerEvent = new MagicEvent(
                    event.getSource(),
                    event.getPlayer(),
                    new MagicMayChoice(
                            "You may pay " + cost.getText(),
                            new MagicPayManaCostChoice(cost)),
                    MagicEvent.NO_DATA,
                    EVENT,
                    "You may$ pay " + cost.getText() +
                    "$. If you don't, sacrifice " + event.getSource() + "."
                    );
            game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(triggerEvent)));            
        }
    };
}
