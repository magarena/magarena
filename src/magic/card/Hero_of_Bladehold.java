package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayCardAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Hero_of_Bladehold {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player=permanent.getController();
            return (permanent==creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        this,
                        player + " puts two 1/1 white Soldier creature tokens " + 
                        "onto the battlefield tapped and attacking."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPlayer player=event.getPlayer();
            for (int count=2;count>0;count--) {
                final MagicCard card=MagicCard.createTokenCard(TokenCardDefinitions.get("Soldier"),player);
                game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.TAPPED_ATTACKING));
            }
        }        
    };
}
