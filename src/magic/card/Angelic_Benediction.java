package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.target.MagicTapTargetPicker;
import magic.model.action.MagicTapAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Angelic_Benediction {
    //Whenever a creature you control attacks alone, you may tap target creature.
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player=permanent.getController();
            return (creature.getController()==player&&player.getNrOfAttackers()==1) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            player + " may tap target creature.",
                            MagicTargetChoice.NEG_TARGET_CREATURE),
                        new MagicTapTargetPicker(true,false),
                        MagicEvent.NO_DATA,
                        this,
                        player + " may$ tap target creature$."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicTapAction(creature,true));
                    }
                });
            }
        }
    };
}
