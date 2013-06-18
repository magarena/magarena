package magic.model.event;

import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.MagicGame;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.action.MagicSetKickerAction;

public class MagicKickerCost extends MagicAdditionalCost implements MagicEventAction {
    
    final MagicManaCost manaCost;
    
    public MagicKickerCost(final MagicManaCost aManaCost) {
        manaCost = aManaCost;
    }

    public MagicKickerCost(final String cost) {
        manaCost = MagicManaCost.create(cost);
    }
    
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicMayChoice(
                "Pay the kicker " + manaCost.getText() + '?',
                new MagicPayManaCostChoice(manaCost)
            ),
            this,
            "PN may$ pay " + manaCost.getText() + "$."
        );
    }
        
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new MagicSetKickerAction(event.getCard(), 1));
        }
    }
}
