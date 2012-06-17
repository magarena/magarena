package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Promise_of_Bunrei {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player=permanent.getController();
            return (otherPermanent.isCreature()&&otherPermanent.getController()==player) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player},
                        this,
                        "Sacrifice " + permanent + ". If you do, " + 
                        "put four 1/1 colorless Spirit creature tokens onto the battlefield."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent=(MagicPermanent)data[0];
            final MagicPlayer player=(MagicPlayer)data[1];
            if (player.controlsPermanent(permanent)) {
                game.doAction(new MagicSacrificeAction(permanent));
                for (int count=4;count>0;count--) {
                    game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Spirit1")));
                }
            }
        }
    };
}
