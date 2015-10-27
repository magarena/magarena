package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicCounterType;
import magic.model.action.TurnFaceUpAction;
import magic.model.action.ChangeCountersAction;
import java.util.List;
import magic.model.MagicMessage;

public class MagicMegamorphActivation extends MagicMorphActivation {
    
    public MagicMegamorphActivation(final List<MagicMatchedCostEvent> aMatchedCostEvents) {
        super(aMatchedCostEvents, "Megamorph");
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new TurnFaceUpAction(event.getPermanent()));
        game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, 1));
        game.logAppendMessage(
            event.getPlayer(),
            String.format("%s turns %s face up and put a +1/+1 counter on it.",
                event.getPlayer(),
                MagicMessage.getCardToken(event.getPermanent())
            )
        );
    }
}
