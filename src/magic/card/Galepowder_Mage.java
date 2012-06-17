package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.MagicExileUntilEndOfTurnAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Galepowder_Mage {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            return (permanent == creature &&
                    game.getNrOfPermanents(MagicType.Creature) > 1) ?
                    new MagicEvent(
                            permanent,
                            player,
                            MagicTargetChoice.TARGET_CREATURE,
                            MagicExileTargetPicker.create(),
                            MagicEvent.NO_DATA,
                            this,
                            "Exile target creature$. Return that card to the " +
                            "battlefield under its owner's control at end of turn.") :
                    MagicEvent.NONE;         
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicExileUntilEndOfTurnAction(creature));
                }
            });
        }
    };
}
