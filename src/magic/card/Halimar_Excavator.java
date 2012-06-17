package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicMillLibraryAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Halimar_Excavator {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            return (otherPermanent.getController() == player &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                        permanent,
                        player,
                        MagicTargetChoice.NEG_TARGET_PLAYER,
                        new Object[]{player},
                        this,
                        "Target player$ puts the top X cards of his or her " +
                        "library into his or her graveyard, where X is the " +
                        "number of Allies " + player + " controls.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                    final MagicPlayer player = (MagicPlayer)data[0];
                    final int amount =
                            player.getNrOfPermanentsWithSubType(MagicSubType.Ally);
                    if (amount > 0) {
                        game.doAction(new MagicMillLibraryAction(targetPlayer,amount));
                    }
                }
            });        
        }        
    };
}
