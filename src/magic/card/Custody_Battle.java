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
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedCreature();
            return enchanted.isController(upkeepPlayer) ?
                new MagicEvent(
                    enchanted,
                    new MagicMayChoice(
                        MagicTargetChoice.SACRIFICE_LAND
                    ),
                    this,
                    "PN may$ sacrifice a land$. If you don't, " +
                    upkeepPlayer.getOpponent() + " gains control of SN.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        game.doAction(new MagicSacrificeAction(perm));
                    }
                });
            } else {
                game.doAction(new MagicGainControlAction(
                    event.getPlayer().getOpponent(),
                    event.getPermanent()
                ));
            }
        }
    };
}
