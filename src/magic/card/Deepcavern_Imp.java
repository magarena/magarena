package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Deepcavern_Imp {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer) && 
                    permanent.hasState(MagicPermanentState.MustPayEchoCost)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may$ discard a card. " +
                    "If he or she doesn't, sacrifice SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            if (event.isYes() && event.getPlayer().getHandSize() > 0) {
                game.addEvent(new MagicDiscardEvent(
                        permanent,
                        event.getPlayer(),
                        1,
                        false));
                game.doAction(new MagicChangeStateAction(
                        permanent,
                        MagicPermanentState.MustPayEchoCost,false));
            } else {
                game.doAction(new MagicSacrificeAction(permanent));
            }
        }        
    };
}
