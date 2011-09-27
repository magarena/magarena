package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Geist_of_Saint_Traft {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
			return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " puts a 4/4 white Angel creature token with " +
                        "flying onto the battlefield tapped and attacking. " +
                        "Exile that token at end of combat."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			final MagicCard card = MagicCard.createTokenCard(TokenCardDefinitions.ANGEL4_TOKEN_CARD,player);
			final MagicPlayCardAction action = new MagicPlayCardAction(card,player,MagicPlayCardAction.TAPPED_ATTACKING);
			//game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.TAPPED_ATTACKING));
			game.doAction(action);
			game.doAction(new MagicChangeStateAction(action.getPermanent(),MagicPermanentState.ExileAtEndOfCombat,true));
		}		
    };
}
