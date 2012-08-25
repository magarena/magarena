package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Custody_Battle {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPermanent enchanted = permanent.getEnchantedCreature();
            final MagicPlayer player = enchanted.getController();
            final MagicPlayer opponent = player.getOpponent();
            return (player == data) ?
                new MagicEvent(
                        enchanted,
                        player,
                        new MagicMayChoice(
                                "You may sacrifice a land. If you don't, " +
                                opponent + " gains control of " + enchanted + ".",
                                MagicTargetChoice.SACRIFICE_LAND),
                        new Object[]{opponent},
                        this,
                        "You may$ sacrifice a land$. If you don't, " +
                        opponent + " gains control of " + enchanted + ".") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        game.doAction(new MagicSacrificeAction(perm));
                    }
                });
            } else {
                game.doAction(new MagicGainControlAction((MagicPlayer)data[0],(MagicPermanent)event.getSource()));
            }
        }
    };
}
