package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Fumiko_the_Lowblood {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            return flags | MagicAbility.AttacksEachTurnIfAble.getMask();
        }
    };
        
    public static final MagicWhenBlocksTrigger T1 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            if (permanent == data) {
                final MagicPlayer player = permanent.getController();
                final int amount = game.getOpponent(player).getNrOfAttackers();
                return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,amount},
                        this,
                        permanent + " gets +" + amount + "/+" +  amount + " until end of turn.");
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(
                    (MagicPermanent)data[0],
                    (Integer)data[1],
                    (Integer)data[1]));
        }
    };
    
    public static final MagicWhenBecomesBlockedTrigger T2 = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            if (permanent == data) {
                final MagicPlayer player = permanent.getController();
                final int amount = player.getNrOfAttackers();
                return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,amount},
                        this,
                        permanent + " gets +" + amount + "/+" +  amount + " until end of turn.");
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(
                    (MagicPermanent)data[0],
                    (Integer)data[1],
                    (Integer)data[1]));
        }
    };
}
