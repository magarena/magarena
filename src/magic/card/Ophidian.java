package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

public class Ophidian {
    public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                final MagicPlayer player = permanent.getController();
                return new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                            player + " may draw a card.",
                            MagicSimpleMayChoice.DRAW_CARDS,
                            1,
                            MagicSimpleMayChoice.DEFAULT_NONE),
                        this,
                        player + " may$ draw a card.");
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicDrawAction(event.getPlayer(),1));
                game.doAction(new MagicChangeStateAction(
                        event.getPermanent(),
                        MagicPermanentState.NoCombatDamage,true));
            }
        }
    };
}
