package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Sheoldred__Whispering_One {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPlayer player=permanent.getController();
            return (player == upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(true),
                    this,
                    "Return target creature card$ from your graveyard to the battlefield."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicReanimateAction(event.getPlayer(),card,MagicPlayCardAction.NONE));
                }
            });
        }
    };
    
    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPlayer player=permanent.getController();
            return (player != upkeepPlayer) ? 
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    this,
                    upkeepPlayer + " sacrifices a creature."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer opponent=event.getPlayer().getOpponent();
            if (opponent.controlsPermanentWithType(MagicType.Creature)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                            event.getSource(),
                            opponent,
                            MagicTargetChoice.SACRIFICE_CREATURE));
            }
        }
    };
}
