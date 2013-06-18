package magic.model.event;

import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.MagicGame;
import magic.model.choice.MagicKickerChoice;
import magic.model.action.MagicSetKickerAction;

public class MagicMultikickerCost extends MagicAdditionalCost implements MagicEventAction {
    
    final MagicManaCost manaCost;
    
    public MagicMultikickerCost(final MagicManaCost aManaCost) {
        manaCost = aManaCost;
    }

    public MagicMultikickerCost(final String cost) {
        manaCost = MagicManaCost.create(cost);
    }
    
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicKickerChoice(manaCost),
            this,
            "PN may pay " + manaCost.getText() + " any number of times$$."
        );
    }
        
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.payManaCost(game,event.getPlayer());
        if (event.isKicked()) {
            game.doAction(new MagicSetKickerAction(event.getCard(), event.getKickerCount()));
        }
    }
}
