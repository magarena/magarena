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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player=permanent.getController();
            return (player != data) ?
                MagicEvent.NONE :
                new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(true),
                    new Object[]{player},
                    this,
                    "Return target creature card$ from your graveyard to the battlefield.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicReanimateAction((MagicPlayer)data[0],card,MagicPlayCardAction.NONE));
                }
            });
        }
    };
    
    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player=permanent.getController();
            return (player == data) ? 
                MagicEvent.NONE :
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent,data},
                    this,
                    "Your opponent sacrifices a creature.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer opponent=(MagicPlayer)data[1];
            if (opponent.controlsPermanentWithType(MagicType.Creature)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                            (MagicPermanent)data[0],
                            opponent,
                            MagicTargetChoice.SACRIFICE_CREATURE));
            }
        }
    };
}
