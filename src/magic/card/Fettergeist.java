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
    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
            if (MagicMayChoice.isNoChoice(choiceResults[0])) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    };
    
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            final MagicPlayer player = permanent.getController();
            return (player == upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Sacrifice SN unless you pay " +
                    "{1} for each other creature you control.") :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int x = event.getPlayer().controlsPermanent(event.getPermanent()) ? 1 : 0;
            final int amount = event.getPlayer().getNrOfPermanentsWithType(MagicType.Creature) - x;
            final MagicManaCost cost = MagicManaCost.create("{"+amount+"}");
            final MagicEvent triggerEvent = new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicMayChoice(
                    "You may pay " + cost.getText(),
                    new MagicPayManaCostChoice(cost)),
                EVENT_ACTION,
                "You may$ pay " + cost.getText() +
                "$. If you don't, sacrifice SN."
                );
            game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(triggerEvent)));            
        }
    };
}
