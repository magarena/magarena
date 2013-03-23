package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;

public class MagicEchoTrigger extends MagicAtUpkeepTrigger {

    private final MagicManaCost manaCost;
    
    public MagicEchoTrigger(final MagicManaCost manaCost) {
        this.manaCost = manaCost;
    }
    
    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer upkeepPlayer) {
        return (permanent.isController(upkeepPlayer) &&
                permanent.hasState(MagicPermanentState.MustPayEchoCost)) ?
            new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(manaCost)
                ),
                this,
                "PN may$ pay " + manaCost +  ". If he or she doesn't, sacrifice SN."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        final MagicPermanent permanent = event.getPermanent();
        if (event.isYes()) {
            game.doAction(new MagicChangeStateAction(
                permanent,
                MagicPermanentState.MustPayEchoCost,false
            ));
        } else {
            game.doAction(new MagicSacrificeAction(permanent));
        }
    }
}
